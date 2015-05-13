package com.cmpe277.groupay;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.net.Uri;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalItem;
import com.paypal.android.sdk.payments.PayPalOAuthScopes;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalProfileSharingActivity;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class ItemTabFragment extends Fragment {

    private static final String TAG = "ItemTabFragment";
    private static final String ITEM_BOUGHT_DIALOG_TAG ="Item bought dialog tag";
    private static final String SUGGEST_DIALOG_TAG ="Item suggest dialog tag";
    private static final String BUY_DIALOG_TAG = "Buy dialog tag";
    private Event mEvent;
    private int mEventIdx;
    //paypal stuffs
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;
    //private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
    private static final String CONFIG_CLIENT_ID = "ahruizguerra-facilitator@hotmail.com";

    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    private static final int REQUEST_CODE_PROFILE_SHARING = 3;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
                    // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("Example Merchant")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));

    public static ItemTabFragment newInstance (int eventNum){
        Bundle args = new Bundle();
        ItemTabFragment fragment = new ItemTabFragment();
        args.putInt(EventActivity.EVENT_INDEX,eventNum);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if(getArguments() != null){
            mEventIdx = getArguments().getInt(EventActivity.EVENT_INDEX);
        }
        else{
            mEventIdx = ((EventActivity)getActivity()).getCurrentEventIndex();
        }
        Log.d(TAG,"current event index "+ mEventIdx);
        mEvent = Data.get().getEvent(mEventIdx);

        // start paypal intent
        Intent intent = new Intent(ItemTabFragment.this.getActivity(), PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        ItemTabFragment.this.getActivity().startService(intent);
    }

    @Override
    public void onDestroy() {
        // Stop service when done
        ItemTabFragment.this.getActivity().stopService(
                new Intent(ItemTabFragment.this.getActivity(), PayPalService.class));
        super.onDestroy();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_event, container, false);
        ListView itemListView = (ListView) v.findViewById(R.id.event_list);
        Button youOweButton = (Button) v.findViewById(R.id.you_owe_button);


        Log.d(TAG, "I see " + mEventIdx);
        if(mEvent.getEventStatus() == Event.EVENT_STATUS.close){
            youOweButton.setVisibility(View.INVISIBLE);
        }
        else{
            youOweButton.setVisibility(View.VISIBLE);
        }

        youOweButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);
                Intent intent = new Intent(v.getContext(), PaymentActivity.class);

                // send the same configuration for restart resiliency
                intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

                intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

                startActivityForResult(intent, REQUEST_CODE_PAYMENT);
            }
        });

       ItemListAdapter itemArrayAdapter=
                new ItemListAdapter( getActivity(),
                        android.R.layout.simple_list_item_1,
                        mEvent.getItemList(),
                        BitmapFactory.decodeResource(getResources(),R.drawable.ic_item));

        itemListView.setAdapter(itemArrayAdapter);
        ItemSelected itemSelected = new ItemSelected();
        itemListView.setOnItemClickListener( itemSelected );

        itemListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Item item = mEvent.getItemAtIndex(position);
                switch (item.getItemStatus()) {
                    case newItem:
                    case waitForVote:
                    case waitToBeBuy:
                        return displayItemQuantityInfo(position);
                    case bought:
                        return requestForApproval(position);
                    default:
                        return true;
                }
            }
        });

        Data.get().getEvent(mEventIdx).setItemListAdaptor(itemArrayAdapter);

        return v;
    }

    public class ItemSelected  implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView parent, View view, final int position, long id) {
            final Item item = mEvent.getItemAtIndex(position);
            final FragmentManager fm = getFragmentManager();

            switch (item.getItemStatus()){
                case newItem:
                    if (item.getItemPermission() == Item.itemDecideEnum.member) {
                        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                        View newItemView = layoutInflater.inflate(R.layout.dialog_yes_no, null);
                        TextView textView = (TextView) newItemView.findViewById(R.id.dialog_yesno_text_view);
                        textView.setText(getResources().getText(R.string.would_you_take_ownership));

                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

                        dialogBuilder.setView(newItemView);
                        dialogBuilder
                                .setPositiveButton("Yes",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                                item.setItemStatus(Item.itemStatusEnum.waitToBeBuy);
                                                item.setItemOwnership(Data.get().getMyName());
                                            }
                                        })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                        dialogBuilder.create().show();
                    }
                    else{
                        SuggestItemInfoFragment fragment = SuggestItemInfoFragment.newInstance(mEventIdx,position);
                        fragment.show(fm, SUGGEST_DIALOG_TAG);
                    }
                    break;
                case waitForVote:
                    Intent i = new Intent(getActivity(),ItemInfoActivity.class);
                    i.putExtra(ItemInfoActivity.EVENT_INDEX, mEventIdx);
                    i.putExtra(ItemInfoActivity.ITEM_INDEX, position);
                    startActivity(i);
                    break;
                case bought:
                    ItemInfoDisplayFragment fragment
                            = ItemInfoDisplayFragment.newInstance(mEventIdx, position, -1, true);
                    fragment.show(fm, ITEM_BOUGHT_DIALOG_TAG);
                    break;
                case waitToBeBuy:
                    //TODO need to check if the person who browse this info is the owner or not
                    LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                    View newItemView = layoutInflater.inflate(R.layout.dialog_yes_no, null);
                    TextView textView = (TextView) newItemView.findViewById(R.id.dialog_yesno_text_view);
                    textView.setText(getResources().getText(R.string.have_you_bought));

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

                    dialogBuilder.setView(newItemView);
                    dialogBuilder
                            .setPositiveButton("Yes",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            BuyItemFragment buyItemFragment
                                                    = BuyItemFragment.newInstance(mEventIdx, position);
                                            buyItemFragment.show(fm, BUY_DIALOG_TAG);
                                        }
                                    })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ItemInfoDisplayFragment infoDisplayFragment
                                            = ItemInfoDisplayFragment.newInstance(mEventIdx, position, -1, false);
                                    infoDisplayFragment.show(fm, ITEM_BOUGHT_DIALOG_TAG);
                                }
                            });
                    dialogBuilder.create().show();
                    break;
                case requestProof:
                    BuyItemFragment buyItemFragment
                            = BuyItemFragment.newInstance(mEventIdx, position);
                    buyItemFragment.show(fm, BUY_DIALOG_TAG);
                    break;

                case approved:
                    ItemInfoDisplayFragment itemInfoDisplayFragment
                            = ItemInfoDisplayFragment.newInstance(mEventIdx,position, -1, true);
                    itemInfoDisplayFragment.show(fm, ITEM_BOUGHT_DIALOG_TAG);
                default:
                    Log.d(TAG, "Not defined");
                    break;
            }

        }
    }

    private boolean displayItemQuantityInfo(int position){

        final Item item = mEvent.getItemAtIndex(position);
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View newItemView = layoutInflater.inflate(R.layout.dialog_item_info, null);
        final EditText quantityText
                = (EditText) newItemView.findViewById(R.id.item_quantity_text);
        final RadioButton groupButton
                = (RadioButton) newItemView.findViewById(R.id.group_radio_button);
        final RadioButton memberButton
                = (RadioButton) newItemView.findViewById(R.id.member_radio_button);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        if (item.getItemPermission() == Item.itemDecideEnum.group){
            groupButton.setChecked(true);
            memberButton.setChecked(false);
        }
        else if (item.getItemPermission() == Item.itemDecideEnum.member){
            groupButton.setChecked(false);
            memberButton.setChecked(true);
        }
        quantityText.setText(Integer.toString(item.getItemQuantity()));
        groupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memberButton.setChecked(false);
            }
        });
        memberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupButton.setChecked(false);
            }
        });

        dialogBuilder.setView(newItemView);
        dialogBuilder
                .setTitle(getResources().getString(R.string.item_info).toString())
                .setPositiveButton("Enter",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (groupButton.isChecked()) {
                                    item.setItemPermission(Item.itemDecideEnum.group);
                                } else if (memberButton.isChecked()) {
                                    item.setItemPermission(Item.itemDecideEnum.member);
                                    if (item.getItemStatus() == Item.itemStatusEnum.waitForVote) {
                                        item.setItemStatus(Item.itemStatusEnum.newItem);
                                        Data.get().getEvent(mEventIdx).getItemListAdaptor().notifyDataSetChanged();
                                    }
                                }
                                item.setItemQuantity(Integer.parseInt(quantityText.getText().toString()));
                                dialog.cancel();
                            }
                        })
                .setCancelable(true);
        dialogBuilder.create().show();
        return true;
    }
    private boolean requestForApproval(int position){
        //TODO need to check ownership?
        //if (!manager) return true;

        final Item item = mEvent.getItemAtIndex(position);
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
        View newItemView = layoutInflater.inflate(R.layout.dialog_request_for_approve, null);
        final CheckBox storeName = (CheckBox) newItemView.findViewById(R.id.store_check_box);
        final CheckBox receipt = (CheckBox)newItemView.findViewById(R.id.receipt_check_box);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setView(newItemView);
        dialogBuilder
                .setPositiveButton("Request Proof",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (storeName.isChecked()){
                                    Log.d(TAG, "Request Store name/link"); //TODO send to buyer's notification
                                }
                                if(receipt.isChecked()){
                                    Log.d(TAG, "Request request"); //TODO send to buyer's notification
                                }
                                if(storeName.isChecked()|| receipt.isChecked()){
                                    item.setItemStatus(Item.itemStatusEnum.requestProof);
                                    mEvent.getItemListAdaptor().notifyDataSetChanged();
                                }
                            }
                        })
                .setNegativeButton("Approved Request",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                item.setItemStatus(Item.itemStatusEnum.approved);
                                Data.get().getEvent(mEventIdx)
                                        .addEventExpense(item.getItemBought().getItemPrice());
                                Data.get().getEvent(mEventIdx).getItemListAdaptor().notifyDataSetChanged();
                            }
                        });
        dialogBuilder.create().show();
        return true;

    }

    // paypal private functions
    private PayPalPayment getThingToBuy(String paymentIntent) {
        return new PayPalPayment(new BigDecimal("1.75"), "USD", "sample item",
                paymentIntent);
    }

    private PayPalOAuthScopes getOauthScopes() {
        /* create the set of required scopes*/
        Set<String> scopes = new HashSet<String>(
                Arrays.asList(PayPalOAuthScopes.PAYPAL_SCOPE_EMAIL, PayPalOAuthScopes.PAYPAL_SCOPE_ADDRESS) );
        return new PayPalOAuthScopes(scopes);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm =
                        data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        //Log.i(TAG, confirm.toJSONObject().toString(4));
                       // Log.i(TAG, confirm.getPayment().toJSONObject().toString(4));
                        /*
                         *  TODO: send 'confirm' (and possibly confirm.getPayment() to your server for verification
                         * or consent completion.
                         * See https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                         * for more details.
                         *
                         * For sample mobile backend interactions, see
                         * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
                         */
                        Toast.makeText(
                                ItemTabFragment.this.getActivity(),
                                "PaymentConfirmation info received from PayPal", Toast.LENGTH_LONG)
                                .show();

                    } catch (Exception e) {
                        Log.e(TAG, "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(TAG, "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        TAG,
                        "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        } else if (requestCode == REQUEST_CODE_FUTURE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization auth =
                        data.getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
                if (auth != null) {
                    try {
                        Log.i("FuturePaymentExample", auth.toJSONObject().toString(4));

                        String authorization_code = auth.getAuthorizationCode();
                        Log.i("FuturePaymentExample", authorization_code);

                        sendAuthorizationToServer(auth);
                        Toast.makeText(
                                ItemTabFragment.this.getActivity(),
                                "Future Payment code received from PayPal", Toast.LENGTH_LONG)
                                .show();

                    } catch (JSONException e) {
                        Log.e("FuturePaymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("FuturePaymentExample", "The user canceled.");
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        "FuturePaymentExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            }
        } else if (requestCode == REQUEST_CODE_PROFILE_SHARING) {
            if (resultCode == Activity.RESULT_OK) {
                PayPalAuthorization auth =
                        data.getParcelableExtra(PayPalProfileSharingActivity.EXTRA_RESULT_AUTHORIZATION);
                if (auth != null) {
                    try {
                        Log.i("ProfileSharingExample", auth.toJSONObject().toString(4));

                        String authorization_code = auth.getAuthorizationCode();
                        Log.i("ProfileSharingExample", authorization_code);

                        sendAuthorizationToServer(auth);
                        Toast.makeText(
                                ItemTabFragment.this.getActivity(),
                                "Profile Sharing code received from PayPal", Toast.LENGTH_LONG)
                                .show();

                    } catch (JSONException e) {
                        Log.e("ProfileSharingExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("ProfileSharingExample", "The user canceled.");
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        "ProfileSharingExample",
                        "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
            }
        }
    }

    private void sendAuthorizationToServer(PayPalAuthorization authorization) {
        ;
    }

}

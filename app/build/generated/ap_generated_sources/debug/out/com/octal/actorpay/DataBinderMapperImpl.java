package com.octal.actorpay;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import androidx.databinding.DataBinderMapper;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.octal.actorpay.databinding.ActivityMainBindingImpl;
import com.octal.actorpay.databinding.ActivitySplashScreenBindingImpl;
import com.octal.actorpay.databinding.AdapterHistoryItemBindingImpl;
import com.octal.actorpay.databinding.AdapterTransactionBindingImpl;
import com.octal.actorpay.databinding.AdapterWalletstatementBindingImpl;
import com.octal.actorpay.databinding.CommonToolbarBindingImpl;
import com.octal.actorpay.databinding.ContentEventBindingImpl;
import com.octal.actorpay.databinding.CustomDuoViewFooterBindingImpl;
import com.octal.actorpay.databinding.CustomDuoViewHeaderBindingImpl;
import com.octal.actorpay.databinding.FragmentHistoryBottomBindingImpl;
import com.octal.actorpay.databinding.FragmentHomeBindingImpl;
import com.octal.actorpay.databinding.FragmentHomeBottomBindingImpl;
import com.octal.actorpay.databinding.FragmentLoginBindingImpl;
import com.octal.actorpay.databinding.FragmentMiscBindingImpl;
import com.octal.actorpay.databinding.FragmentMyOrderListBindingImpl;
import com.octal.actorpay.databinding.FragmentNotificationBindingImpl;
import com.octal.actorpay.databinding.FragmentProductsListBindingImpl;
import com.octal.actorpay.databinding.FragmentProfileBottomBindingImpl;
import com.octal.actorpay.databinding.FragmentRewardsPointsBindingImpl;
import com.octal.actorpay.databinding.FragmentSplashBindingImpl;
import com.octal.actorpay.databinding.FragmentTransferMoneyBindingImpl;
import com.octal.actorpay.databinding.FragmentWalletBottomBindingImpl;
import com.octal.actorpay.databinding.FragmentWalletwStatementBindingImpl;
import com.octal.actorpay.databinding.ItemFeaturesBindingImpl;
import com.octal.actorpay.databinding.LayoutMainBindingImpl;
import com.octal.actorpay.databinding.LoginScreenFragmentBindingImpl;
import com.octal.actorpay.databinding.SignUpScreenFragmentBindingImpl;
import com.octal.actorpay.databinding.ToolbarTransBindingImpl;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.RuntimeException;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBinderMapperImpl extends DataBinderMapper {
  private static final int LAYOUT_ACTIVITYMAIN = 1;

  private static final int LAYOUT_ACTIVITYSPLASHSCREEN = 2;

  private static final int LAYOUT_ADAPTERHISTORYITEM = 3;

  private static final int LAYOUT_ADAPTERTRANSACTION = 4;

  private static final int LAYOUT_ADAPTERWALLETSTATEMENT = 5;

  private static final int LAYOUT_COMMONTOOLBAR = 6;

  private static final int LAYOUT_CONTENTEVENT = 7;

  private static final int LAYOUT_CUSTOMDUOVIEWFOOTER = 8;

  private static final int LAYOUT_CUSTOMDUOVIEWHEADER = 9;

  private static final int LAYOUT_FRAGMENTHISTORYBOTTOM = 10;

  private static final int LAYOUT_FRAGMENTHOME = 11;

  private static final int LAYOUT_FRAGMENTHOMEBOTTOM = 12;

  private static final int LAYOUT_FRAGMENTLOGIN = 13;

  private static final int LAYOUT_FRAGMENTMISC = 14;

  private static final int LAYOUT_FRAGMENTMYORDERLIST = 15;

  private static final int LAYOUT_FRAGMENTNOTIFICATION = 16;

  private static final int LAYOUT_FRAGMENTPRODUCTSLIST = 17;

  private static final int LAYOUT_FRAGMENTPROFILEBOTTOM = 18;

  private static final int LAYOUT_FRAGMENTREWARDSPOINTS = 19;

  private static final int LAYOUT_FRAGMENTSPLASH = 20;

  private static final int LAYOUT_FRAGMENTTRANSFERMONEY = 21;

  private static final int LAYOUT_FRAGMENTWALLETBOTTOM = 22;

  private static final int LAYOUT_FRAGMENTWALLETWSTATEMENT = 23;

  private static final int LAYOUT_ITEMFEATURES = 24;

  private static final int LAYOUT_LAYOUTMAIN = 25;

  private static final int LAYOUT_LOGINSCREENFRAGMENT = 26;

  private static final int LAYOUT_SIGNUPSCREENFRAGMENT = 27;

  private static final int LAYOUT_TOOLBARTRANS = 28;

  private static final SparseIntArray INTERNAL_LAYOUT_ID_LOOKUP = new SparseIntArray(28);

  static {
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.octal.actorpay.R.layout.activity_main, LAYOUT_ACTIVITYMAIN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.octal.actorpay.R.layout.activity_splash_screen, LAYOUT_ACTIVITYSPLASHSCREEN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.octal.actorpay.R.layout.adapter_history_item, LAYOUT_ADAPTERHISTORYITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.octal.actorpay.R.layout.adapter_transaction, LAYOUT_ADAPTERTRANSACTION);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.octal.actorpay.R.layout.adapter_walletstatement, LAYOUT_ADAPTERWALLETSTATEMENT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.octal.actorpay.R.layout.common_toolbar, LAYOUT_COMMONTOOLBAR);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.octal.actorpay.R.layout.content_event, LAYOUT_CONTENTEVENT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.octal.actorpay.R.layout.custom_duo_view_footer, LAYOUT_CUSTOMDUOVIEWFOOTER);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.octal.actorpay.R.layout.custom_duo_view_header, LAYOUT_CUSTOMDUOVIEWHEADER);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.octal.actorpay.R.layout.fragment_history_bottom, LAYOUT_FRAGMENTHISTORYBOTTOM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.octal.actorpay.R.layout.fragment_home, LAYOUT_FRAGMENTHOME);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.octal.actorpay.R.layout.fragment_home_bottom, LAYOUT_FRAGMENTHOMEBOTTOM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.octal.actorpay.R.layout.fragment_login, LAYOUT_FRAGMENTLOGIN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.octal.actorpay.R.layout.fragment_misc, LAYOUT_FRAGMENTMISC);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.octal.actorpay.R.layout.fragment_my_order_list, LAYOUT_FRAGMENTMYORDERLIST);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.octal.actorpay.R.layout.fragment_notification, LAYOUT_FRAGMENTNOTIFICATION);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.octal.actorpay.R.layout.fragment_products_list, LAYOUT_FRAGMENTPRODUCTSLIST);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.octal.actorpay.R.layout.fragment_profile_bottom, LAYOUT_FRAGMENTPROFILEBOTTOM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.octal.actorpay.R.layout.fragment_rewards_points, LAYOUT_FRAGMENTREWARDSPOINTS);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.octal.actorpay.R.layout.fragment_splash, LAYOUT_FRAGMENTSPLASH);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.octal.actorpay.R.layout.fragment_transfer_money, LAYOUT_FRAGMENTTRANSFERMONEY);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.octal.actorpay.R.layout.fragment_wallet_bottom, LAYOUT_FRAGMENTWALLETBOTTOM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.octal.actorpay.R.layout.fragment_walletw_statement, LAYOUT_FRAGMENTWALLETWSTATEMENT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.octal.actorpay.R.layout.item_features, LAYOUT_ITEMFEATURES);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.octal.actorpay.R.layout.layout_main, LAYOUT_LAYOUTMAIN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.octal.actorpay.R.layout.login_screen_fragment, LAYOUT_LOGINSCREENFRAGMENT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.octal.actorpay.R.layout.sign_up_screen_fragment, LAYOUT_SIGNUPSCREENFRAGMENT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.octal.actorpay.R.layout.toolbar_trans, LAYOUT_TOOLBARTRANS);
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View view, int layoutId) {
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = view.getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
        case  LAYOUT_ACTIVITYMAIN: {
          if ("layout/activity_main_0".equals(tag)) {
            return new ActivityMainBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_main is invalid. Received: " + tag);
        }
        case  LAYOUT_ACTIVITYSPLASHSCREEN: {
          if ("layout/activity_splash_screen_0".equals(tag)) {
            return new ActivitySplashScreenBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_splash_screen is invalid. Received: " + tag);
        }
        case  LAYOUT_ADAPTERHISTORYITEM: {
          if ("layout/adapter_history_item_0".equals(tag)) {
            return new AdapterHistoryItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for adapter_history_item is invalid. Received: " + tag);
        }
        case  LAYOUT_ADAPTERTRANSACTION: {
          if ("layout/adapter_transaction_0".equals(tag)) {
            return new AdapterTransactionBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for adapter_transaction is invalid. Received: " + tag);
        }
        case  LAYOUT_ADAPTERWALLETSTATEMENT: {
          if ("layout/adapter_walletstatement_0".equals(tag)) {
            return new AdapterWalletstatementBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for adapter_walletstatement is invalid. Received: " + tag);
        }
        case  LAYOUT_COMMONTOOLBAR: {
          if ("layout/common_toolbar_0".equals(tag)) {
            return new CommonToolbarBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for common_toolbar is invalid. Received: " + tag);
        }
        case  LAYOUT_CONTENTEVENT: {
          if ("layout/content_event_0".equals(tag)) {
            return new ContentEventBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for content_event is invalid. Received: " + tag);
        }
        case  LAYOUT_CUSTOMDUOVIEWFOOTER: {
          if ("layout/custom_duo_view_footer_0".equals(tag)) {
            return new CustomDuoViewFooterBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for custom_duo_view_footer is invalid. Received: " + tag);
        }
        case  LAYOUT_CUSTOMDUOVIEWHEADER: {
          if ("layout/custom_duo_view_header_0".equals(tag)) {
            return new CustomDuoViewHeaderBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for custom_duo_view_header is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTHISTORYBOTTOM: {
          if ("layout/fragment_history_bottom_0".equals(tag)) {
            return new FragmentHistoryBottomBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_history_bottom is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTHOME: {
          if ("layout/fragment_home_0".equals(tag)) {
            return new FragmentHomeBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_home is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTHOMEBOTTOM: {
          if ("layout/fragment_home_bottom_0".equals(tag)) {
            return new FragmentHomeBottomBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_home_bottom is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTLOGIN: {
          if ("layout/fragment_login_0".equals(tag)) {
            return new FragmentLoginBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_login is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTMISC: {
          if ("layout/fragment_misc_0".equals(tag)) {
            return new FragmentMiscBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_misc is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTMYORDERLIST: {
          if ("layout/fragment_my_order_list_0".equals(tag)) {
            return new FragmentMyOrderListBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_my_order_list is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTNOTIFICATION: {
          if ("layout/fragment_notification_0".equals(tag)) {
            return new FragmentNotificationBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_notification is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTPRODUCTSLIST: {
          if ("layout/fragment_products_list_0".equals(tag)) {
            return new FragmentProductsListBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_products_list is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTPROFILEBOTTOM: {
          if ("layout/fragment_profile_bottom_0".equals(tag)) {
            return new FragmentProfileBottomBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_profile_bottom is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTREWARDSPOINTS: {
          if ("layout/fragment_rewards_points_0".equals(tag)) {
            return new FragmentRewardsPointsBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_rewards_points is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTSPLASH: {
          if ("layout/fragment_splash_0".equals(tag)) {
            return new FragmentSplashBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_splash is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTTRANSFERMONEY: {
          if ("layout/fragment_transfer_money_0".equals(tag)) {
            return new FragmentTransferMoneyBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_transfer_money is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTWALLETBOTTOM: {
          if ("layout/fragment_wallet_bottom_0".equals(tag)) {
            return new FragmentWalletBottomBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_wallet_bottom is invalid. Received: " + tag);
        }
        case  LAYOUT_FRAGMENTWALLETWSTATEMENT: {
          if ("layout/fragment_walletw_statement_0".equals(tag)) {
            return new FragmentWalletwStatementBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for fragment_walletw_statement is invalid. Received: " + tag);
        }
        case  LAYOUT_ITEMFEATURES: {
          if ("layout/item_features_0".equals(tag)) {
            return new ItemFeaturesBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for item_features is invalid. Received: " + tag);
        }
        case  LAYOUT_LAYOUTMAIN: {
          if ("layout/layout_main_0".equals(tag)) {
            return new LayoutMainBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for layout_main is invalid. Received: " + tag);
        }
        case  LAYOUT_LOGINSCREENFRAGMENT: {
          if ("layout/login_screen_fragment_0".equals(tag)) {
            return new LoginScreenFragmentBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for login_screen_fragment is invalid. Received: " + tag);
        }
        case  LAYOUT_SIGNUPSCREENFRAGMENT: {
          if ("layout/sign_up_screen_fragment_0".equals(tag)) {
            return new SignUpScreenFragmentBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for sign_up_screen_fragment is invalid. Received: " + tag);
        }
        case  LAYOUT_TOOLBARTRANS: {
          if ("layout/toolbar_trans_0".equals(tag)) {
            return new ToolbarTransBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for toolbar_trans is invalid. Received: " + tag);
        }
      }
    }
    return null;
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View[] views, int layoutId) {
    if(views == null || views.length == 0) {
      return null;
    }
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = views[0].getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
      }
    }
    return null;
  }

  @Override
  public int getLayoutId(String tag) {
    if (tag == null) {
      return 0;
    }
    Integer tmpVal = InnerLayoutIdLookup.sKeys.get(tag);
    return tmpVal == null ? 0 : tmpVal;
  }

  @Override
  public String convertBrIdToString(int localId) {
    String tmpVal = InnerBrLookup.sKeys.get(localId);
    return tmpVal;
  }

  @Override
  public List<DataBinderMapper> collectDependencies() {
    ArrayList<DataBinderMapper> result = new ArrayList<DataBinderMapper>(1);
    result.add(new androidx.databinding.library.baseAdapters.DataBinderMapperImpl());
    return result;
  }

  private static class InnerBrLookup {
    static final SparseArray<String> sKeys = new SparseArray<String>(1);

    static {
      sKeys.put(0, "_all");
    }
  }

  private static class InnerLayoutIdLookup {
    static final HashMap<String, Integer> sKeys = new HashMap<String, Integer>(28);

    static {
      sKeys.put("layout/activity_main_0", com.octal.actorpay.R.layout.activity_main);
      sKeys.put("layout/activity_splash_screen_0", com.octal.actorpay.R.layout.activity_splash_screen);
      sKeys.put("layout/adapter_history_item_0", com.octal.actorpay.R.layout.adapter_history_item);
      sKeys.put("layout/adapter_transaction_0", com.octal.actorpay.R.layout.adapter_transaction);
      sKeys.put("layout/adapter_walletstatement_0", com.octal.actorpay.R.layout.adapter_walletstatement);
      sKeys.put("layout/common_toolbar_0", com.octal.actorpay.R.layout.common_toolbar);
      sKeys.put("layout/content_event_0", com.octal.actorpay.R.layout.content_event);
      sKeys.put("layout/custom_duo_view_footer_0", com.octal.actorpay.R.layout.custom_duo_view_footer);
      sKeys.put("layout/custom_duo_view_header_0", com.octal.actorpay.R.layout.custom_duo_view_header);
      sKeys.put("layout/fragment_history_bottom_0", com.octal.actorpay.R.layout.fragment_history_bottom);
      sKeys.put("layout/fragment_home_0", com.octal.actorpay.R.layout.fragment_home);
      sKeys.put("layout/fragment_home_bottom_0", com.octal.actorpay.R.layout.fragment_home_bottom);
      sKeys.put("layout/fragment_login_0", com.octal.actorpay.R.layout.fragment_login);
      sKeys.put("layout/fragment_misc_0", com.octal.actorpay.R.layout.fragment_misc);
      sKeys.put("layout/fragment_my_order_list_0", com.octal.actorpay.R.layout.fragment_my_order_list);
      sKeys.put("layout/fragment_notification_0", com.octal.actorpay.R.layout.fragment_notification);
      sKeys.put("layout/fragment_products_list_0", com.octal.actorpay.R.layout.fragment_products_list);
      sKeys.put("layout/fragment_profile_bottom_0", com.octal.actorpay.R.layout.fragment_profile_bottom);
      sKeys.put("layout/fragment_rewards_points_0", com.octal.actorpay.R.layout.fragment_rewards_points);
      sKeys.put("layout/fragment_splash_0", com.octal.actorpay.R.layout.fragment_splash);
      sKeys.put("layout/fragment_transfer_money_0", com.octal.actorpay.R.layout.fragment_transfer_money);
      sKeys.put("layout/fragment_wallet_bottom_0", com.octal.actorpay.R.layout.fragment_wallet_bottom);
      sKeys.put("layout/fragment_walletw_statement_0", com.octal.actorpay.R.layout.fragment_walletw_statement);
      sKeys.put("layout/item_features_0", com.octal.actorpay.R.layout.item_features);
      sKeys.put("layout/layout_main_0", com.octal.actorpay.R.layout.layout_main);
      sKeys.put("layout/login_screen_fragment_0", com.octal.actorpay.R.layout.login_screen_fragment);
      sKeys.put("layout/sign_up_screen_fragment_0", com.octal.actorpay.R.layout.sign_up_screen_fragment);
      sKeys.put("layout/toolbar_trans_0", com.octal.actorpay.R.layout.toolbar_trans);
    }
  }
}

// Generated by data binding compiler. Do not edit!
package com.octal.actorpay.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.octal.actorpay.R;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class AdapterTransactionBinding extends ViewDataBinding {
  @NonNull
  public final TextView dateText;

  @NonNull
  public final ImageView itemImage;

  @NonNull
  public final TextView itemTitle;

  @NonNull
  public final ConstraintLayout notificationImageLayout;

  @NonNull
  public final TextView priceText;

  protected AdapterTransactionBinding(Object _bindingComponent, View _root, int _localFieldCount,
      TextView dateText, ImageView itemImage, TextView itemTitle,
      ConstraintLayout notificationImageLayout, TextView priceText) {
    super(_bindingComponent, _root, _localFieldCount);
    this.dateText = dateText;
    this.itemImage = itemImage;
    this.itemTitle = itemTitle;
    this.notificationImageLayout = notificationImageLayout;
    this.priceText = priceText;
  }

  @NonNull
  public static AdapterTransactionBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.adapter_transaction, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static AdapterTransactionBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<AdapterTransactionBinding>inflateInternal(inflater, R.layout.adapter_transaction, root, attachToRoot, component);
  }

  @NonNull
  public static AdapterTransactionBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.adapter_transaction, null, false, component)
   */
  @NonNull
  @Deprecated
  public static AdapterTransactionBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<AdapterTransactionBinding>inflateInternal(inflater, R.layout.adapter_transaction, null, false, component);
  }

  public static AdapterTransactionBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.bind(view, component)
   */
  @Deprecated
  public static AdapterTransactionBinding bind(@NonNull View view, @Nullable Object component) {
    return (AdapterTransactionBinding)bind(component, view, R.layout.adapter_transaction);
  }
}

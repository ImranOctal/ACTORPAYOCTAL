// Generated by data binding compiler. Do not edit!
package com.octal.actorpay.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.octal.actorpay.R;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class ToolbarTransBinding extends ViewDataBinding {
  @NonNull
  public final Toolbar toolbar;

  @NonNull
  public final TextView txtToolbarTitle;

  protected ToolbarTransBinding(Object _bindingComponent, View _root, int _localFieldCount,
      Toolbar toolbar, TextView txtToolbarTitle) {
    super(_bindingComponent, _root, _localFieldCount);
    this.toolbar = toolbar;
    this.txtToolbarTitle = txtToolbarTitle;
  }

  @NonNull
  public static ToolbarTransBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.toolbar_trans, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static ToolbarTransBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<ToolbarTransBinding>inflateInternal(inflater, R.layout.toolbar_trans, root, attachToRoot, component);
  }

  @NonNull
  public static ToolbarTransBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.toolbar_trans, null, false, component)
   */
  @NonNull
  @Deprecated
  public static ToolbarTransBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<ToolbarTransBinding>inflateInternal(inflater, R.layout.toolbar_trans, null, false, component);
  }

  public static ToolbarTransBinding bind(@NonNull View view) {
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
  public static ToolbarTransBinding bind(@NonNull View view, @Nullable Object component) {
    return (ToolbarTransBinding)bind(component, view, R.layout.toolbar_trans);
  }
}

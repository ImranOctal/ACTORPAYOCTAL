package com.octal.actorpay.databinding;
import com.octal.actorpay.R;
import com.octal.actorpay.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentLoginBindingImpl extends FragmentLoginBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.logo, 1);
        sViewsWithIds.put(R.id.tabs, 2);
        sViewsWithIds.put(R.id.header_LinearLayout, 3);
        sViewsWithIds.put(R.id.view_pager, 4);
        sViewsWithIds.put(R.id.txtOR2, 5);
        sViewsWithIds.put(R.id.linearLayout, 6);
        sViewsWithIds.put(R.id.imGoogle, 7);
        sViewsWithIds.put(R.id.imFacebook, 8);
        sViewsWithIds.put(R.id.imTwitter, 9);
        sViewsWithIds.put(R.id.txtViewSignUp, 10);
    }
    // views
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentLoginBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 11, sIncludes, sViewsWithIds));
    }
    private FragmentLoginBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.LinearLayout) bindings[3]
            , (android.widget.ImageView) bindings[8]
            , (android.widget.ImageView) bindings[7]
            , (android.widget.ImageView) bindings[9]
            , (android.widget.LinearLayout) bindings[6]
            , (androidx.appcompat.widget.AppCompatImageView) bindings[1]
            , (androidx.core.widget.NestedScrollView) bindings[0]
            , (com.google.android.material.tabs.TabLayout) bindings[2]
            , (androidx.appcompat.widget.AppCompatTextView) bindings[5]
            , (androidx.appcompat.widget.AppCompatTextView) bindings[10]
            , (androidx.viewpager.widget.ViewPager) bindings[4]
            );
        this.scrollable.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x1L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
            return variableSet;
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        // batch finished
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}
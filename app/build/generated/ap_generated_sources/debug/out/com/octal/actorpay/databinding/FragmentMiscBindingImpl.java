package com.octal.actorpay.databinding;
import com.octal.actorpay.R;
import com.octal.actorpay.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentMiscBindingImpl extends FragmentMiscBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = new androidx.databinding.ViewDataBinding.IncludedLayouts(12);
        sIncludes.setIncludes(0, 
            new String[] {"common_toolbar"},
            new int[] {1},
            new int[] {com.octal.actorpay.R.layout.common_toolbar});
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.aboutUsText, 2);
        sViewsWithIds.put(R.id.about_us_icon, 3);
        sViewsWithIds.put(R.id.contactUsText, 4);
        sViewsWithIds.put(R.id.contactUsImage, 5);
        sViewsWithIds.put(R.id.faqText, 6);
        sViewsWithIds.put(R.id.faqImage, 7);
        sViewsWithIds.put(R.id.tcText, 8);
        sViewsWithIds.put(R.id.tcImage, 9);
        sViewsWithIds.put(R.id.privacyText, 10);
        sViewsWithIds.put(R.id.privacyImage, 11);
    }
    // views
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentMiscBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 12, sIncludes, sViewsWithIds));
    }
    private FragmentMiscBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 1
            , (android.widget.ImageView) bindings[3]
            , (androidx.cardview.widget.CardView) bindings[2]
            , (android.widget.ImageView) bindings[5]
            , (androidx.cardview.widget.CardView) bindings[4]
            , (android.widget.ImageView) bindings[7]
            , (androidx.cardview.widget.CardView) bindings[6]
            , (android.widget.ImageView) bindings[11]
            , (androidx.cardview.widget.CardView) bindings[10]
            , (android.widget.ImageView) bindings[9]
            , (androidx.cardview.widget.CardView) bindings[8]
            , (com.octal.actorpay.databinding.CommonToolbarBinding) bindings[1]
            );
        this.mboundView0 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        setContainedBinding(this.toolbar);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x2L;
        }
        toolbar.invalidateAll();
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        if (toolbar.hasPendingBindings()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
            return variableSet;
    }

    @Override
    public void setLifecycleOwner(@Nullable androidx.lifecycle.LifecycleOwner lifecycleOwner) {
        super.setLifecycleOwner(lifecycleOwner);
        toolbar.setLifecycleOwner(lifecycleOwner);
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeToolbar((com.octal.actorpay.databinding.CommonToolbarBinding) object, fieldId);
        }
        return false;
    }
    private boolean onChangeToolbar(com.octal.actorpay.databinding.CommonToolbarBinding Toolbar, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
            }
            return true;
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
        executeBindingsOn(toolbar);
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): toolbar
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}
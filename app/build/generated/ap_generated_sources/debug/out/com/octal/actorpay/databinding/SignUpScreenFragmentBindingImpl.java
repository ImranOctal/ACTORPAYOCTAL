package com.octal.actorpay.databinding;
import com.octal.actorpay.R;
import com.octal.actorpay.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class SignUpScreenFragmentBindingImpl extends SignUpScreenFragmentBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.filled_exposed_dropdown, 1);
        sViewsWithIds.put(R.id.cardViewMobile, 2);
        sViewsWithIds.put(R.id.ccp, 3);
        sViewsWithIds.put(R.id.divider, 4);
        sViewsWithIds.put(R.id.editTextMobile, 5);
        sViewsWithIds.put(R.id.firstName, 6);
        sViewsWithIds.put(R.id.lastName, 7);
        sViewsWithIds.put(R.id.email, 8);
        sViewsWithIds.put(R.id.password, 9);
        sViewsWithIds.put(R.id.buttonSignUp, 10);
    }
    // views
    @NonNull
    private final android.widget.LinearLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public SignUpScreenFragmentBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 11, sIncludes, sViewsWithIds));
    }
    private SignUpScreenFragmentBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (com.google.android.material.button.MaterialButton) bindings[10]
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[2]
            , (com.hbb20.CountryCodePicker) bindings[3]
            , (android.view.View) bindings[4]
            , (com.google.android.material.textfield.TextInputEditText) bindings[5]
            , (androidx.appcompat.widget.AppCompatEditText) bindings[8]
            , (com.google.android.material.textfield.MaterialAutoCompleteTextView) bindings[1]
            , (androidx.appcompat.widget.AppCompatEditText) bindings[6]
            , (androidx.appcompat.widget.AppCompatEditText) bindings[7]
            , (androidx.appcompat.widget.AppCompatTextView) bindings[9]
            );
        this.mboundView0 = (android.widget.LinearLayout) bindings[0];
        this.mboundView0.setTag(null);
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
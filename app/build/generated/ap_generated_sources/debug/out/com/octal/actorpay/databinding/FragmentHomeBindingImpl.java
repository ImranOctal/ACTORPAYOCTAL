package com.octal.actorpay.databinding;
import com.octal.actorpay.R;
import com.octal.actorpay.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentHomeBindingImpl extends FragmentHomeBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = new androidx.databinding.ViewDataBinding.IncludedLayouts(6);
        sIncludes.setIncludes(1, 
            new String[] {"toolbar_trans"},
            new int[] {3},
            new int[] {com.octal.actorpay.R.layout.toolbar_trans});
        sIncludes.setIncludes(2, 
            new String[] {"layout_main"},
            new int[] {4},
            new int[] {com.octal.actorpay.R.layout.layout_main});
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.duoMenuView, 5);
    }
    // views
    @NonNull
    private final android.widget.RelativeLayout mboundView1;
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView2;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentHomeBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 6, sIncludes, sViewsWithIds));
    }
    private FragmentHomeBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 2
            , (nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout) bindings[0]
            , (nl.psdcompany.duonavigationdrawer.views.DuoMenuView) bindings[5]
            , (com.octal.actorpay.databinding.LayoutMainBinding) bindings[4]
            , (com.octal.actorpay.databinding.ToolbarTransBinding) bindings[3]
            );
        this.drawer.setTag(null);
        setContainedBinding(this.layoutMainID);
        this.mboundView1 = (android.widget.RelativeLayout) bindings[1];
        this.mboundView1.setTag(this.mboundView1.getResources().getString(com.octal.actorpay.R.string.tag_content));
        this.mboundView2 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[2];
        this.mboundView2.setTag(null);
        setContainedBinding(this.toolbarLayout);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x4L;
        }
        toolbarLayout.invalidateAll();
        layoutMainID.invalidateAll();
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        if (toolbarLayout.hasPendingBindings()) {
            return true;
        }
        if (layoutMainID.hasPendingBindings()) {
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
        toolbarLayout.setLifecycleOwner(lifecycleOwner);
        layoutMainID.setLifecycleOwner(lifecycleOwner);
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeToolbarLayout((com.octal.actorpay.databinding.ToolbarTransBinding) object, fieldId);
            case 1 :
                return onChangeLayoutMainID((com.octal.actorpay.databinding.LayoutMainBinding) object, fieldId);
        }
        return false;
    }
    private boolean onChangeToolbarLayout(com.octal.actorpay.databinding.ToolbarTransBinding ToolbarLayout, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
            }
            return true;
        }
        return false;
    }
    private boolean onChangeLayoutMainID(com.octal.actorpay.databinding.LayoutMainBinding LayoutMainID, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x2L;
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
        executeBindingsOn(toolbarLayout);
        executeBindingsOn(layoutMainID);
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): toolbarLayout
        flag 1 (0x2L): layoutMainID
        flag 2 (0x3L): null
    flag mapping end*/
    //end
}
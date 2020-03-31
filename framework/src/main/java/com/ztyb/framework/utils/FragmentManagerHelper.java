package com.ztyb.framework.utils;


import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2018/1/23.
 */

public class FragmentManagerHelper {
    private FragmentManager mFragmentManager;
    private int mContainerViewId;

    public FragmentManagerHelper(@Nullable FragmentManager fragmentManager, @IdRes int containerViewId) {
        this.mFragmentManager = fragmentManager;
        this.mContainerViewId = containerViewId;
    }

    public void add(Fragment fragment) {
        FragmentTransaction fragmentTransaction = this.mFragmentManager.beginTransaction();
        fragmentTransaction.add(this.mContainerViewId, fragment);
        fragmentTransaction.commit();
    }

    public void add(Fragment fragment, String tag) {
        FragmentTransaction fragmentTransaction = this.mFragmentManager.beginTransaction();
        fragmentTransaction.add(this.mContainerViewId, fragment, tag);
        fragmentTransaction.commit();
    }

    public void switchFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        List<Fragment> childFragments = mFragmentManager.getFragments();
        Iterator var4 = childFragments.iterator();

        while (var4.hasNext()) {
            Fragment childFragment = (Fragment) var4.next();
            fragmentTransaction.hide(childFragment);
        }

        if (!childFragments.contains(fragment)) {
            fragmentTransaction.add(this.mContainerViewId, fragment);
        } else {
            fragmentTransaction.show(fragment);
        }

        fragmentTransaction.commit();
    }


}

package com.rezafaaldiansyah_5210411314.inventoryapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ExpensePagerAdapter extends FragmentStateAdapter {

    public ExpensePagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new TodayExpenseFragment(); // Halaman untuk pengeluaran hari ini
            case 1:
                return new MonthExpenseFragment(); // Halaman untuk pengeluaran bulan ini
            default:
                return new TodayExpenseFragment(); // Default ke halaman hari ini
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Dua halaman, satu untuk hari ini dan satu untuk bulan ini
    }
}

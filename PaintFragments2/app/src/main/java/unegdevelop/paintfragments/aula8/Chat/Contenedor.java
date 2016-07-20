package unegdevelop.paintfragments.aula8.Chat;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import unegdevelop.paintfragments.R;

/**
 * Created by xavier on 25/06/16.
 */
public class Contenedor extends Fragment
{
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FragmentActivity activity;
    private View view;

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        activity = (FragmentActivity)context;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.contenedor, container, false);

        inicializarTabs();
        return view;
    }




    private void inicializarTabs()
    {
        toolbar = (Toolbar)view.findViewById(R.id.toolbar);
        //((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        //((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabLayout = (TabLayout)view.findViewById(R.id.tabs);
        viewPager = (ViewPager)view.findViewById(R.id.pager);

        viewPager.setAdapter(new SectionPagerAdapter(activity.getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }


    public class SectionPagerAdapter extends FragmentPagerAdapter
    {
        public SectionPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }


        @Override
        public Fragment getItem(int position)
        {
            switch (position)
            {
                case 0:
                    return new FragmentChat();
                case 1:
                    return new FragmentConectados();
                default:
                    return new FragmentProfesor();


            }

        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            switch (position)
            {
                case 0: return "Preguntas";
                case 1: return "Conectados";
                default: return "Profesor";


            }
        }
    }
}


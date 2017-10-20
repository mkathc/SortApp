package com.kath.sortapp.presentation.sort;

import android.content.Context;
import android.support.annotation.NonNull;

import com.kath.sortapp.data.entities.ElementEntity;
import com.kath.sortapp.data.local.SortDataSource;
import com.kath.sortapp.data.local.SortRepository;
import com.kath.sortapp.data.local.preferences.SessionManager;
import com.kath.sortapp.data.local.sqlite.SortDbHelper;
import com.kath.sortapp.presentation.sort.listener.ElementItem;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.support.v4.util.Preconditions.checkNotNull;

/**
 * Created by katherine on 14/10/17.
 */

public class SortPresenter implements SortContract.Presenter, ElementItem {

    private SortContract.View mView;
    private Context context;
    private SessionManager mSessionManager;
    private SortRepository mSortRepository;


    public SortPresenter(@NonNull SortContract.View mView,@NonNull Context context, @NonNull SortRepository mSortRepository) {
        this.context = context;
        this.mView = mView;
        this.mView.setPresenter(this);
        this.mSessionManager = new SessionManager(this.context);
        this.mSortRepository = mSortRepository;
    }

    @Override
    public void start() {
    }

    @Override
    public void LoadListElements(final boolean type) {
        mSortRepository.getELements(new SortDataSource.LoadElementsCallback() {
            @Override
            public void onElementsLoaded(final List<ElementEntity> elementEntities) {
                // CÓDIGO ORDENAR
                // BurbleSort();
                //Sort(elementEntities);
                if (type){
                    //Collections.sort(elementEntities);
                    if (validateList(elementEntities)){
                        List<ElementEntity> sortList = bubleSort(elementEntities);
                        refreshData(sortList);
                    }else {
                        mView.getElementsSuccessful(elementEntities);

                    }

                }else{
                    mView.getElementsSuccessful(elementEntities);
                }
            }

            @Override
            public void onDataNotAvailable() {

            }
        });
    }

    public boolean validateList(List<ElementEntity> list){
        for (int i = 0; i <list.size() ; i++) {

            if (list.get(i).getCount()>=3){

                return true;
            }

        }
        return false;
    }


    public void refreshData(List<ElementEntity> list){
        mSortRepository.refreshElements(list);
        LoadListElements(false);

    }

    public List<ElementEntity> bubleSort(List<ElementEntity> list){
        ElementEntity temp;
        if (list.size()>1) // check if the number of orders is larger than 1
        {
            for (int x=0; x<list.size(); x++) // bubble sort outer loop
            {
                for (int i=0; i < list.size()- x - 1; i++) {
                    if (list.get(i).compareTo(list.get(i+1)) > 0)
                    {
                        temp = list.get(i);
                        list.set(i,list.get(i+1) );
                        list.set(i+1, temp);
                    }
                }
            }
        }
        return list;
    }
public void resetCount(List<ElementEntity> list){

    for (int i = 0; i <list.size() ; i++) {
        mSortRepository.updateElements(list.get(i).getId(), 0 , list.get(i).getTouch());
    }

}
    @Override
    public void saveElement(ElementEntity elementEntity) {
        save(elementEntity);
    }

    @Override
    public void sortList(List<ElementEntity> list) {
    }

    public void save(ElementEntity elementEntity){
        mSortRepository.saveElement(elementEntity);
    }


    @Override
    public void updateElement(String id, int count, String last_touch) {
        mSortRepository.updateElements(id, count, last_touch);
    }


    @Override
    public void clickItem(ElementEntity elementEntity) {
        mView.clickElement(elementEntity);
    }

    @Override
    public void deleteItem(ElementEntity elementEntity, int position) {
    }
}

package com.manbuit.android.fragment;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.manbuit.android.fragment.adapter.StdListAdapter;
import com.manbuit.android.fragment.dataRequest.Column;
import com.manbuit.android.fragment.dataRequest.DataRequest;
import com.manbuit.android.fragment.dataRequest.DataRequestUnit;
import com.manbuit.android.fragment.dataRequest.Filter;
import com.manbuit.android.fragment.dataRequest.LoadData;
import com.manbuit.android.fragment.dataRequest.OrderBy;
import com.manbuit.android.fragment.model.StdEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AccountFragment extends ListFragment implements SwipeRefreshLayout.OnRefreshListener, LoadData {
//public class AccountFragment extends SwipeRefreshListFragment {
    final static int PAGE_SIZE = 50;

    private StdApp global;

    ListView accountListView;
    SwipeRefreshLayout accountLayout;

    BaseAdapter adapter;
    List<StdEntity> stdEntities;

    //RequestQueue queue;

    Toolbar toolbar;
    SearchView searchView;

    String lastSearchQuery = "";

    final Handler loadDataHandler = new Handler(){
        public void handleMessage(Message msg){

            //stdEntities = new ArrayList<>();
            try {
                JSONObject result = (JSONObject) msg.obj;
                JSONObject data = result.getJSONObject("data");

                JSONArray dataList = data.getJSONArray("data");
                for(int i = 0; i<dataList.length();i++){
                    JSONObject item = dataList.getJSONObject(i);
                    String id = item.getString("id");
                    String code = item.getString("code");
                    String name = item.getString("name");
                    String status = item.getString("status");
                    StdEntity stdEntity = new StdEntity(id,code,name,status);
                    stdEntities.add(stdEntity);
                }

                if(dataList.length()>0) {
                    adapter.notifyDataSetChanged();
                    if(adapter.getCount()<=PAGE_SIZE) {
                        Toast.makeText(getActivity(), String.format("共计 %d 条记录",data.getInt("totalCount")), Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    //Toast.makeText(getActivity(), "已经加载全部数据", Toast.LENGTH_SHORT).show();
                }

                accountLayout.setRefreshing(false);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    /*void loadData(int start, int limit, String searchText, final Handler handler) {
        if(start==0){
            stdEntities.clear();
        }
        accountLayout.setRefreshing(true);

        final DataRequest dataRequest = new DataRequest();
        DataRequestUnit data = new DataRequestUnit();

        //data.setDs("12814070-5537-b95c-e4f9-abfc0d460765");
        data.setDs("e1e89bf9-5924-5061-2834-9475d355355d");
        data.setFields(Arrays.asList(
                new Column("id"),
                new Column("code"),
                new Column("name"),
                new Column("status")
        ));
        data.setOrderbies(Arrays.asList(
                new OrderBy("category_orderNo", true),
                new OrderBy("orderNo1", true),
                new OrderBy("orderNo2", true),
                new OrderBy("orderNo3", true)
        ));
        data.setStart(start);
        data.setLimit(limit);

        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("orgId","string","!=","NULL",null));
        if(searchText!=null && searchText.trim().length()>0) {
            for (String seg : searchText.trim().replaceAll(" +", " ").split(" ")) {
                if (seg.length() > 0) {
                    Filter filter = new Filter("or", null, null, null,
                            Arrays.asList(
                                    new Filter("code", "string", ">", "'" + seg + "'", null),
                                    new Filter("name", "string", ">", "'" + seg + "'", null),
                                    new Filter("category_name", "string", ">", "'" + seg + "'", null)
                            )
                    );
                    filters.add(filter);
                }
            }
        }
        data.setFilter(new Filter("and", null, null, null, filters));

        dataRequest.getNodes().put("data", data);

        dataRequest.request(global,loadDataHandler);
    }*/

    void loadData(int start, int limit, Bundle queryCond, final Handler handler) {
        if(start==0){
            stdEntities.clear();
        }
        accountLayout.setRefreshing(true);

        final DataRequest dataRequest = new DataRequest();
        DataRequestUnit data = new DataRequestUnit();

        data.setDs("e1e89bf9-5924-5061-2834-9475d355355d");
        data.setFields(Arrays.asList(
                new Column("id"),
                new Column("code"),
                new Column("name"),
                new Column("status")
        ));
        data.setOrderbies(Arrays.asList(
                new OrderBy("category_orderNo", true),
                new OrderBy("orderNo1", true),
                new OrderBy("orderNo2", true),
                new OrderBy("orderNo3", true)
        ));
        data.setStart(start);
        data.setLimit(limit);

        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("orgId","string","!=","NULL",null));

        if(queryCond!=null) {
            String status = queryCond.getString("status");
            String category = queryCond.getString("category");
            String namecode = queryCond.getString("namecode");

            if (namecode != null && namecode.trim().length() > 0) {
                for (String seg : namecode.trim().replaceAll(" +", " ").split(" ")) {
                    if (seg.length() > 0) {
                        Filter filter = new Filter("or", null, null, null,
                                Arrays.asList(
                                        new Filter("code", "string", ">", "'" + seg + "'", null),
                                        new Filter("name", "string", ">", "'" + seg + "'", null),
                                        new Filter("category_name", "string", ">", "'" + seg + "'", null)
                                )
                        );
                        filters.add(filter);
                    }
                }
            }

            if (status != null && status.length() > 0) {
                filters.add(new Filter("status", "string", "=", "'" + status + "'", null));
            }

            if (category != null && category.length() > 0) {
                filters.add(new Filter("category_name", "string", "=", "'" + category + "'", null));
            }
        }
        data.setFilter(new Filter("and", null, null, null, filters));

        dataRequest.getNodes().put("data", data);

        dataRequest.request(global,loadDataHandler);
    }

    public void loadData(Bundle queryCond){
        loadData(0, PAGE_SIZE, queryCond, loadDataHandler);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View accountLayout = inflater.inflate(R.layout.fragment_account, container, false);
        return accountLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        global = (StdApp) getActivity().getApplication();

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("部门台账");

        accountLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.accountLayout);
        accountLayout.setOnRefreshListener(this);

        accountListView = getListView();
        stdEntities = new ArrayList<>();

        adapter = new StdListAdapter(getActivity(),stdEntities);
        setListAdapter(adapter);

        //queue = Volley.newRequestQueue(getActivity());

        accountListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int lastItemIndex;//当前ListView中最后一个Item的索引

            @Override
            public void onScrollStateChanged(AbsListView arg0, int scrollState) {
                //System.out.println("onScrollStateChanged");
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && lastItemIndex == adapter.getCount() - 1) {
                    //System.out.println(String.format("onScrollStateChanged LastVisiblePosition: %d", accountListView.getLastVisiblePosition()));

                    // TODO 这里需要"加载更多"的代码
                    loadData(adapter.getCount(), PAGE_SIZE, ((MainActivity) getActivity()).queryConds.get(AccountFragment.class.getName()), loadDataHandler);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItemIndex = firstVisibleItem + visibleItemCount - 1;
                //System.out.println(String.format("onScroll LastVisiblePosition: %d", accountListView.getLastVisiblePosition()));
                /*if (accountListView.getCount() != 0
                        && accountListView.getLastVisiblePosition() >= (accountListView.getCount() - 1) - 2) {
                    // Do what you need to get more content.
                    //Toast.makeText(getActivity(), "加载更多...", Toast.LENGTH_SHORT).show();
                    System.out.println(String.format("LastVisiblePosition: %d", accountListView.getLastVisiblePosition()));
                }*/
            }
        });

        accountListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent();
                intent.putExtra("stdId", stdEntities.get(position).getId());
                intent.setClassName(getActivity(), "com.manbuit.android.fragment.StdDetailActivity");
                startActivity(intent);
            }
        });

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        searchView = (SearchView) toolbar.getMenu().findItem(R.id.action_search).getActionView();
        searchView.setQuery(lastSearchQuery, false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                doSearch(query);
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText == null || newText.trim().length() == 0) {
                    doSearch(null);
                }
                return true;
            }
        });

        loadData(0, PAGE_SIZE, ((MainActivity) getActivity()).queryConds.get(AccountFragment.class.getName()), loadDataHandler);

    }

    private void doSearch(String inputText) {
        stdEntities.clear();
        loadData(0, PAGE_SIZE, ((MainActivity) getActivity()).queryConds.get(AccountFragment.class.getName()), loadDataHandler);
    }

    @Override
    public void onRefresh() {
        loadData(0, PAGE_SIZE, ((MainActivity) getActivity()).queryConds.get(AccountFragment.class.getName()), loadDataHandler);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if(hidden) {
            lastSearchQuery = searchView.getQuery().toString();
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return true;
                }
            });
        }
        else {
            //Toast.makeText(getActivity(), "onHiddenChanged", Toast.LENGTH_SHORT).show();

            searchView.setQuery(lastSearchQuery, false);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    doSearch(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (newText == null || newText.trim().length() == 0) {
                        doSearch(null);
                    }
                    return true;
                }
            });
            /*if (!lastSearchQuery.equals(searchView.getQuery().toString())) {
                searchView.setQuery(searchView.getQuery(), true);
            }*/
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //Toast.makeText(getActivity(),"resume", Toast.LENGTH_SHORT).show();
    }
}

package com.manbuit.android.fragment;

import android.app.ListFragment;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.manbuit.android.fragment.adapter.StdListAdapter;
import com.manbuit.android.fragment.dataRequest.DataRequest;
import com.manbuit.android.fragment.dataRequest.DataRequestUnit;
import com.manbuit.android.fragment.dataRequest.Filter;
import com.manbuit.android.fragment.dataRequest.OrderBy;
import com.manbuit.android.fragment.model.StdEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountFragment extends ListFragment implements SwipeRefreshLayout.OnRefreshListener {
//public class AccountFragment extends SwipeRefreshListFragment {

    private StdApp global;

    ListView accountListView;
    SwipeRefreshLayout accountLayout;

    BaseAdapter adapter;
    List<StdEntity> stdEntities;

    RequestQueue queue;

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
                    StdEntity stdEntity = new StdEntity(id,code,name);
                    stdEntities.add(stdEntity);
                }

                adapter.notifyDataSetChanged();
                accountLayout.setRefreshing(false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    void loadData(int start, int limit, final Handler handler) {
        accountLayout.setRefreshing(true);

        final DataRequest dataRequest = new DataRequest();
        DataRequestUnit data = new DataRequestUnit();

        data.setDs("12814070-5537-b95c-e4f9-abfc0d460765");
        data.setOrderbies(Arrays.asList(
                new OrderBy("category_orderNo",true),
                new OrderBy("orderNo1",true),
                new OrderBy("orderNo2",true),
                new OrderBy("orderNo3",true)
        ));
        data.setStart(start);
        data.setLimit(limit);

        dataRequest.getNodes().put("data", data);

        Request request = dataRequest.genRequest(
                global.getMyContext().get("token").toString(),
                loadDataHandler
        );
        queue.add(request);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //accountListView = (ListView) getActivity().findViewById(R.id.accountListView);
        //accountListView.setAdapter(adapter);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View accountLayout = inflater.inflate(R.layout.activity_account, container, false);
        return accountLayout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        global = (StdApp) getActivity().getApplication();

        accountLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.accountLayout);
        accountLayout.setOnRefreshListener(this);

        accountListView = getListView();
        stdEntities = new ArrayList<>();

        adapter = new StdListAdapter(getActivity(),stdEntities);
        setListAdapter(adapter);

        queue = Volley.newRequestQueue(getActivity());

        //http://fredericosilva.net/blog/listview-with-swiperefreshlayout-and-autoload/
        //http://blog.csdn.net/appte/article/details/10795401
        accountListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int lastItemIndex;//当前ListView中最后一个Item的索引
            @Override
            public void onScrollStateChanged(AbsListView arg0, int scrollState) {
                System.out.println("onScrollStateChanged");
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                        && lastItemIndex == adapter.getCount() - 1) {
                    System.out.println(String.format("onScrollStateChanged LastVisiblePosition: %d", accountListView.getLastVisiblePosition()));

                    // TODO 这里需要"加载更多"的代码
                    loadData(adapter.getCount(),20,loadDataHandler);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItemIndex = firstVisibleItem + visibleItemCount - 1;
                System.out.println(String.format("onScroll LastVisiblePosition: %d", accountListView.getLastVisiblePosition()));
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

        loadData(0,20,loadDataHandler);
    }

    @Override
    public void onRefresh() {
        /*accountLayout.setRefreshing(true);
        Toast.makeText(getActivity(), "重新刷新...", Toast.LENGTH_SHORT).show();

        (new Handler()).postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        accountLayout.setRefreshing(false);
                    }
                },
                3000
        );*/
        loadData(0,20,loadDataHandler);
    }
}

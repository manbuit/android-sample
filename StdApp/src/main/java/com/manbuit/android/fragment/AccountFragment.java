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

    final Handler loadDataHandler = new Handler(){
        public void handleMessage(Message msg){
            JSONObject data = (JSONObject) msg.obj;
            try {
                JSONArray dataList = data.getJSONArray("data");
                for(int i = 0; i<dataList.length();i++){
                    JSONObject item = dataList.getJSONObject(i);
                    String id = item.getString("id");
                    String code = item.getString("code");
                    String name = item.getString("name");
                    StdEntity stdEntity = new StdEntity(id,code,name);
                    stdEntities.add(stdEntity);
                }

                adapter = new StdListAdapter(getActivity(),stdEntities);
                setListAdapter(adapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

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

        //http://fredericosilva.net/blog/listview-with-swiperefreshlayout-and-autoload/
        accountListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (accountListView.getCount() != 0
                        && accountListView.getLastVisiblePosition() >= (accountListView.getCount() - 1) - 2) {
                    // Do what you need to get more content.
                    //Toast.makeText(getActivity(), "加载更多...", Toast.LENGTH_SHORT).show();
                    System.out.println(String.format("LastVisiblePosition: %d", accountListView.getLastVisiblePosition()));
                }
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

        new Thread(new Runnable() {
            @Override
            public void run() {
                final DataRequest dataRequest = new DataRequest();
                DataRequestUnit data = new DataRequestUnit();
                data.setDs("12814070-5537-b95c-e4f9-abfc0d460765"); //角色

                {
                    Filter AND = new Filter();
                    AND.setExp("and");
                    AND.setLeaf(false);
                    List<Filter> AND_children = new ArrayList<Filter>();
                    AND.setChildren(AND_children);
                    {
                        Filter filter = new Filter();
                        filter.setExp("code");
                        filter.setValue("'系统'");
                        filter.setOperate(">");
                        filter.setType("string");
                        //AND_children.add(filter);
                    }
                    {
                        Filter filter = new Filter();
                        filter.setExp("name");
                        filter.setValue("'员'");
                        filter.setOperate(">");
                        filter.setType("string");
                        //AND_children.add(filter);
                    }

                    data.setFilter(AND);
                }
                {
                    List<OrderBy> orderBies = new ArrayList<OrderBy>();

                    OrderBy categoryOrderNo = new OrderBy();
                    //categoryOrderNo.setExp("(select orderNo from jyjy_std_category where id=rptSql.category)");
                    categoryOrderNo.setExp("category_orderNo");
                    categoryOrderNo.setAsc(true);
                    orderBies.add(categoryOrderNo);

                    OrderBy orderNo1 = new OrderBy();
                    orderNo1.setExp("orderNo1");
                    orderNo1.setAsc(true);
                    orderBies.add(orderNo1);

                    OrderBy orderNo2 = new OrderBy();
                    orderNo2.setExp("orderNo2");
                    orderNo2.setAsc(true);
                    orderBies.add(orderNo2);

                    OrderBy orderNo3 = new OrderBy();
                    orderNo3.setExp("orderNo3");
                    orderNo3.setAsc(true);
                    orderBies.add(orderNo3);

                    data.setOrderbies(orderBies);
                }

                dataRequest.getNodes().put("data", data);

                RequestQueue queue = Volley.newRequestQueue(getActivity());

                String url = String.format("%s;jsessionid=%s", global.getDataLoadUrl(), global.getMyContext().get("token"));
                StringRequest request = new StringRequest(
                        Request.Method.POST,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                //Toast.makeText(LoginActivity.this,s,Toast.LENGTH_SHORT).show();
                                try {
                                    JSONObject result = new JSONObject(s);
                                    JSONObject data = result.getJSONObject("data");
                                    //Toast.makeText(getActivity(),data.get("totalCount").toString(),Toast.LENGTH_SHORT).show();
                                    Message msg = new Message();
                                    msg.obj = data;
                                    loadDataHandler.sendMessage(msg);
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                    Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                //showProgress(false);
                                Toast.makeText(getActivity(), volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                ){
                    @Override
                    protected Map<String, String> getParams() {
                        //在这里设置需要post的参数
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("dr", dataRequest.toJSON().toString());
                        return map;
                    }
                };
                queue.add(request);
            }
        }).start();
    }

    @Override
    public void onRefresh() {
        accountLayout.setRefreshing(true);
        Toast.makeText(getActivity(), "重新刷新...", Toast.LENGTH_SHORT).show();

        (new Handler()).postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        accountLayout.setRefreshing(false);
                    }
                },
                3000
        );
    }
}

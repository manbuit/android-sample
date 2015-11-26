package com.manbuit.android.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.manbuit.android.fragment.dataRequest.DataRequest;
import com.manbuit.android.fragment.dataRequest.DataRequestUnit;
import com.manbuit.android.fragment.dataRequest.Filter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class OtherFragment extends Fragment {

    private StdApp global;

    Button btnUpdate;
    Button btnTestDataRequest;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View messageLayout = inflater.inflate(R.layout.activity_other, container, false);
        return messageLayout;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        global = (StdApp) getActivity().getApplication();

        btnUpdate = (Button) getActivity().findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("title");
                builder.setMessage("message");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(),"确定",Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(),"取消",Toast.LENGTH_SHORT).show();
                    }
                });

                builder.create().show();
            }
        });

        btnTestDataRequest = (Button) getActivity().findViewById(R.id.btnTestDataRequest);
        btnTestDataRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DataRequest dataRequest = new DataRequest();
                DataRequestUnit data = new DataRequestUnit();
                data.setDs("2ee611a7-a3b6-4e7a-8f5f-677f0cad8b46"); //角色

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
                        AND_children.add(filter);
                    }
                    {
                        Filter filter = new Filter();
                        filter.setExp("name");
                        filter.setValue("'员'");
                        filter.setOperate(">");
                        filter.setType("string");
                        AND_children.add(filter);
                    }

                    data.setFilter(AND);
                }

                dataRequest.getNodes().put("data", data);

                RequestQueue queue = Volley.newRequestQueue(getActivity());
                String url = String.format("%s;jsessionid=%s",global.getDataLoadUrl(),global.getMyContext().get("token"));

                /*JsonObjectRequest request = new JsonObjectRequest(
                        Request.Method.POST,
                        url,
                        dataRequest.toJSON(),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject obj) {
                                Toast.makeText(getActivity(),obj.toString(),Toast.LENGTH_SHORT).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_SHORT).show();
                                System.out.println(error.getMessage());
                        }
                });*/

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
                                    Toast.makeText(getActivity(),data.get("totalCount").toString(),Toast.LENGTH_SHORT).show();
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
        });
    }
}

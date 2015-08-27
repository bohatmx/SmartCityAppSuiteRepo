package com.boha.library.dto;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aubreyM on 15/08/07.
 */
public class CategoryHardCode {

    public static void main(String[] args) {

        try {
            List<ComplaintCategoryDTO> list = getFaultCategories();
            System.out.println(" categories built: " + list.size());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static List<ComplaintCategoryDTO> getFaultCategories() throws JSONException {
        List<ComplaintCategoryDTO> list = new ArrayList<>();
        try {
            JSONObject object = new JSONObject(json);
            JSONObject cats = object.getJSONObject("categories");
            JSONArray array = cats.getJSONArray("category");
            for (int i = 0; i < array.length(); i++) {
                ComplaintCategoryDTO cc = new ComplaintCategoryDTO();
                JSONObject x = array.getJSONObject(i);
                cc.setId(Integer.parseInt(x.getString("id")));
                cc.setComplaintCategoryName(x.getString("description"));
                cc.setComplaintTypeList(new ArrayList<ComplaintTypeDTO>());

                JSONArray subs = x.getJSONArray("subcategories");
                for (int j = 0; j < subs.length(); j++) {
                    ComplaintTypeDTO v = new ComplaintTypeDTO();
                    JSONObject z = subs.getJSONObject(j);
                    v.setId(Integer.parseInt(z.getString("id")));
                    v.setComplaintTypeName(x.getString("description"));
                    cc.getComplaintTypeList().add(v);
                }

                list.add(cc);
            }
        } catch (RuntimeException ex) {
            Log.e("HardCode", "Failed to get JSON", ex);
        }


        return list;
    }

    private static  String json =
            "{" +
                    "  \"categories\":{" +
                    "    \"category\":[" +
                    "      {" +
                    "        \"id\":\"189\"," +
                    "        \"description\":\"Water\"," +
                    "        \"subcategories\":[" +
                    "          {" +
                    "            \"id\":\"190\"," +
                    "            \"description\":\"Leaking Pipe\"" +
                    "          }," +
                    "          {" +
                    "            \"id\":\"191\"," +
                    "            \"description\":\"Burst\"" +
                    "          }," +
                    "          {" +
                    "            \"id\":\"192\"," +
                    "            \"description\":\"Water Pressure Problem\"" +
                    "          }," +
                    "          {" +
                    "            \"id\":\"193\"," +
                    "            \"description\":\"No Water\"" +
                    "          }" +
                    "        ]" +
                    "      }," +
                    "      {" +
                    "        \"id\":\"197\"," +
                    "        \"description\":\"Pollution \"," +
                    "        \"subcategories\":[" +
                    "          {" +
                    "            \"id\":\"198\"," +
                    "            \"description\":\"Air\"" +
                    "          }," +
                    "          {" +
                    "            \"id\":\"199\"," +
                    "            \"description\":\"Water\"" +
                    "          }," +
                    "          {" +
                    "            \"id\":\"200\"," +
                    "            \"description\":\"Land\"" +
                    "          }," +
                    "          {" +
                    "            \"id\":\"201\"," +
                    "            \"description\":\"Beach\"" +
                    "          }" +
                    "        ]" +
                    "      }," +
                    "      {" +
                    "        \"id\":\"202\"," +
                    "        \"description\":\"Traffic\"," +
                    "        \"subcategories\":[" +
                    "          {" +
                    "            \"id\":\"203\"," +
                    "            \"description\":\"All Lights Out of Order\"" +
                    "          }," +
                    "          {" +
                    "            \"id\":\"204\"," +
                    "            \"description\":\"Flashing\"" +
                    "          }" +
                    "        ]" +
                    "      }," +
                    "      {" +
                    "        \"id\":\"205\"," +
                    "        \"description\":\"Road\"," +
                    "        \"subcategories\":[" +
                    "          {" +
                    "            \"id\":\"206\"," +
                    "            \"description\":\"Pot Hole\"" +
                    "          }," +
                    "          {" +
                    "            \"id\":\"207\"," +
                    "            \"description\":\"Sink Hole\"" +
                    "          }," +
                    "          {" +
                    "            \"id\":\"208\"," +
                    "            \"description\":\"Over flowing\"" +
                    "          }," +
                    "          {" +
                    "            \"id\":\"209\"," +
                    "            \"description\":\"Missing ManHole cover\"" +
                    "          }" +
                    "        ]" +
                    "      }," +
                    "      {" +
                    "        \"id\":\"194\"," +
                    "        \"description\":\"Waste Water \"," +
                    "        \"subcategories\":[" +
                    "          {" +
                    "            \"id\":\"195\"," +
                    "            \"description\":\"Over Flaw\"" +
                    "          }," +
                    "          {" +
                    "            \"id\":\"196\"," +
                    "            \"description\":\"Manhole Cover \"" +
                    "          }" +
                    "        ]" +
                    "      }" +
                    "    ]" +
                    "  }" +
                    "}";
}

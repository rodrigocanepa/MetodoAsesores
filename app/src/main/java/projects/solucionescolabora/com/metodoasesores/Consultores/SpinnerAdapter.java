package projects.solucionescolabora.com.metodoasesores.Consultores;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import projects.solucionescolabora.com.metodoasesores.R;

/**
 * Created by rodrigocanepacruz on 01/11/18.
 */

public class SpinnerAdapter extends BaseAdapter {

    private List<String> especialidades;
    private Activity activity;
    private LayoutInflater layoutInflater;

    public SpinnerAdapter(List<String> especialidades, Activity activity){
        this.especialidades = especialidades;
        this.activity = activity;
        this.layoutInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return especialidades.size();
    }

    @Override
    public Object getItem(int i) {
        return especialidades.get(i);

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view_ = view;
        if(view == null)
            view_ = layoutInflater.inflate(R.layout.spinner_item, null);
        TextView tv = (TextView)view_.findViewById(R.id.customSpinnerItemTextView);
        tv.setText(especialidades.get(i));
        return view_;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        LinearLayout ll = (LinearLayout)view;
        TextView tv = (TextView)ll.findViewById(R.id.customSpinnerItemTextView);
        tv.setTextColor(Color.parseColor("#000000"));
        tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        return view;
    }
}

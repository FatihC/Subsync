package com.rdlab.adapters;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.rdlab.model.AuditLog;
import com.rdlab.subssync.R;
import com.rdlab.utility.Constants;
import com.rdlab.utility.DateUtils;

public class AuditItemAdapter  extends ArrayAdapter<AuditLog> {

	private ArrayList<AuditLog> _auditLogs;
	private ArrayList<AuditLog> _dtAuditLogs;
	private Context _context;
	private Filter filter;

	public AuditItemAdapter(Context context, int resourceId,
			ArrayList<AuditLog> addressList) {
		super(context, resourceId, addressList);
		
		this._auditLogs = addressList;
		this._dtAuditLogs=new ArrayList<AuditLog>(addressList);
		this._context = context;

	}
	
	public ArrayList<AuditLog> getData() {
		return this._auditLogs;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this._auditLogs.size();
	}

	@Override
	public AuditLog getItem(int arg0) {
		// TODO Auto-generated method stub
		return this._auditLogs.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		if (arg1 == null) {
			LayoutInflater inflater = (LayoutInflater) this._context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			arg1 = inflater.inflate(R.layout.audit_list_item, null);
		} 
		AuditLog audit = _auditLogs.get(arg0);

//		TextView txtAuditIndoorNumber = (TextView) arg1.findViewById(R.id.txtAuditIndoorNumber);
		TextView txtAuditSelectionName = (TextView) arg1.findViewById(R.id.txtAuditSelectionName);
		TextView txtAuditCreateDate = (TextView) arg1.findViewById(R.id.txtAuditCreateDate);
		TextView txtAuditProgressStatus = (TextView) arg1.findViewById(R.id.txtAuditProgressStatus);
		TextView txtAuditedCheckStatus = (TextView) arg1.findViewById(R.id.txtAuditedCheckStatus);
		TextView txtAuditStatus = (TextView) arg1.findViewById(R.id.txtAuditStatus);
		
		
		int progressId=Integer.valueOf(audit.AuditProgressStatus);
		int selectionId=Integer.valueOf(audit.AuditOptionSelection);
		if (audit.AuditedCheckStatus!=null&&!audit.AuditedCheckStatus.isEmpty()) {
			int auditedCheckId=Integer.valueOf(audit.AuditedCheckStatus);
			txtAuditedCheckStatus.setText(Constants.STATUSES.get(auditedCheckId));
		}else
		{
			txtAuditedCheckStatus.setText("Enerjisiz Birim");
		}
		
		String status=audit.AuditStatus.equals("1")?"Aktif":"Pasif";
		
//		txtAuditIndoorNumber.setText(audit.IndoorNumber);
		txtAuditSelectionName.setText(Constants.AUDITSELECTION.get(selectionId));
		txtAuditCreateDate.setText(DateUtils.FormatLongToStringDate(audit.CreateDate));
		txtAuditProgressStatus.setText(Constants.AUDITPROGRESSTYPE.get(progressId));
		
		txtAuditStatus.setText(status);
		
		return arg1;
	}

	@Override
	public Filter getFilter() {
		if (filter == null)
			filter = new AppFilter(_dtAuditLogs);
		return filter;
	}

	@SuppressLint("DefaultLocale")
	private class AppFilter extends Filter {

		private ArrayList<AuditLog> items;

		public AppFilter(ArrayList<AuditLog> items) {
			// TODO Auto-generated constructor stub
			this.items = items;
		}

		@Override
		protected FilterResults performFiltering(CharSequence chars) {
			
			FilterResults result = new FilterResults();
			if (chars != null&&!chars.toString().isEmpty() && chars.length() > 0) {
				ArrayList<AuditLog> filter = new ArrayList<AuditLog>();

				for (AuditLog object : items) {
					// the filtering itself:
					if (object.toString().contains(chars))
						filter.add(object);
				}
				result.count = filter.size();
				result.values = filter;
			} else {
				ArrayList<AuditLog> filter = new ArrayList<AuditLog>();
				for (AuditLog object : items) {
					filter.add(object);
				}
				result.count = filter.size();
				result.values = filter;
			}
			return result;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			ArrayList<AuditLog> filtered = (ArrayList<AuditLog>) results.values;
			notifyDataSetChanged();
			clear();
			for (int i = 0, l = filtered.size(); i < l; i++)
				add((AuditLog) filtered.get(i));
			notifyDataSetInvalidated();
		}

	}
}

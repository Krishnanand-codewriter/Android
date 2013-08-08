package edu.uml.cs.isense.proj;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import edu.uml.cs.isense.R;
import edu.uml.cs.isense.comm.API;
import edu.uml.cs.isense.objects.RProject;

public class ProjectAdapter extends ArrayAdapter<RProject> {
	public ArrayList<RProject> items;
	private Context mContext;
	private int resourceID;
	private int loadingRow;
	public int itemsLoaded;
	public boolean allItemsLoaded;
	private Boolean loading;
	private UIUpdateTask updateTask;
	private Handler uiHandler = new Handler();
	private API api;
	public static final int pageSize = 10;
	public int page = 0;
	public String query = "";

	public ProjectAdapter(Context context, int textViewResourceId,
			int loadingRow, ArrayList<RProject> items) {
		super(context, textViewResourceId, items);
		this.items = items;
		mContext = context;
		resourceID = textViewResourceId;
		this.loadingRow = loadingRow;
		itemsLoaded = 0;
		allItemsLoaded = false;
		loading = false;
		updateTask = new UIUpdateTask();
		api = API.getInstance(context);
	}

	public int getCount() {
		int count = itemsLoaded;
		if (!allItemsLoaded)
			++count;
		return count;
	}

	public RProject getItem(int position) {
		RProject result;
		synchronized (items) {
			result = items.get(position);
		}
		return result;
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		boolean isLastRow = position >= itemsLoaded;
		View v = convertView;
		LayoutInflater vi = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if (isLastRow) {
			v = vi.inflate(loadingRow, null);
		} else if (v == null || v.findViewById(R.id.toptext) == null) {
			v = vi.inflate(resourceID, null);
		}

		if (!isLastRow && items.size() != 0) {
			RProject p = items.get(position);
			if (p != null) {
				TextView tt = (TextView) v.findViewById(R.id.toptext);
				TextView bt = (TextView) v.findViewById(R.id.bottomtext);
				if (tt != null) {
					tt.setText(p.name);
				}
				if (bt != null) {
					if (p.owner_name.equals("")) {
						bt.setVisibility(View.GONE);
					} else {
						bt.setText("Created By: " + p.owner_name);
					}
				}
			}
		} else {
			if (!allItemsLoaded) {
				page++;
				synchronized (loading) {
					if (!loading.booleanValue()) {
						loading = Boolean.TRUE;
						Thread t = new LoadingThread();
						t.start();
					}
				}
			} else {
				uiHandler.post(updateTask);
			}
		}
		return v;
	}

	class LoadingThread extends Thread {
		public void run() {
			ArrayList<RProject> new_items = api.getProjects(page, pageSize, true, query);

			if (new_items.size() == 0) {
				allItemsLoaded = true;
			} else {
				synchronized(items) {
					items.addAll(new_items);
				}
				itemsLoaded += new_items.size();
			}
			
			synchronized( loading ) {
				loading = Boolean.FALSE;
			}
			uiHandler.post( updateTask );
		}
	}

	class UIUpdateTask implements Runnable {
		public void run() {
			notifyDataSetChanged();
		}
	}

}
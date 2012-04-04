package uk.ac.bham.cs.bhamnav;

import java.util.ArrayList;

import org.me.nav.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

@SuppressWarnings("rawtypes")
public class NodeOverlay extends ItemizedOverlay {

	private ArrayList<OverlayItem> overlays = new ArrayList<OverlayItem>();
	private ArrayList<Integer> imageresources = new ArrayList<Integer>();

	Context globalcontext;
	AlertDialog dialog;

	public NodeOverlay(Drawable defaultMarker) {
		/**
		 * This will pass the Drawable object to the superclass and will bind
		 * the co-ordinates and draw it such that the image overlay will appear
		 * just above its specified co-ordinates.
		 */
		super(boundCenterBottom(defaultMarker));
	}

	/**
	 * This will handle what happens when a touch event occurs. Passes to
	 * constructor to bind co-ordinates and initialises 'globalcontext' variable
	 * 
	 * @param defaultMarker
	 * @param context
	 */
	public NodeOverlay(Drawable defaultMarker, Context context) {
		super(boundCenterBottom(defaultMarker));
		globalcontext = context;
	}

	/**
	 * This is called when the populate method is called.
	 */
	@Override
	protected OverlayItem createItem(int i) {
		return overlays.get(i);
	}

	@Override
	public int size() {
		return overlays.size();
	}

	public void addNode(OverlayItem overlay) {
		imageresources.add(-1);
		overlays.add(overlay);
		populate(); // this will call the interface ItemizedOverlay's populate
					// method which will
		// help make sure the OverlayItems are ready to be drawn
	}

	public void addNode(OverlayItem overlay, int imageResource) {
		imageresources.add(imageResource);
		overlays.add(overlay);
		populate();
	}

	@Override
	protected boolean onTap(final int item) { // final was added
		
		final OverlayItem node = overlays.get(item);
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(globalcontext);

		dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		dialog.setNegativeButton("Ground View",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						/**
						 * Inspired from developer.android.com Source:
						 * http://developer
						 * .android.com/guide/topics/ui/dialogs.html
						 * #CustomDialog
						 */

						final Dialog imagedialog = new Dialog(globalcontext);
						imagedialog.setTitle(node.getTitle());
						imagedialog.setContentView(R.layout.departmentdialog);
						ImageView image = (ImageView) imagedialog
								.findViewById(R.id.dialogimage);
						try {
							if (imageresources.get(item) != -1) {
								image.setImageResource(imageresources.get(item));
								image.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										imagedialog.dismiss();
									}

								});
							}
						} catch (NullPointerException e) {

						}
						imagedialog.show();

					}
				});

		dialog.setMessage(node.getSnippet());
		dialog.setTitle(node.getTitle());

		AlertDialog result = dialog.create();
		result.show();

		if (imageresources.get(item) == -1) {
			result.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(false);
		} else {
			result.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(true);
		}

		return true;
	}
}

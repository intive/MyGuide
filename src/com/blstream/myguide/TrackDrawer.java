
package com.blstream.myguide;

import java.util.ArrayList;
import java.util.ListIterator;

import com.blstream.myguide.path.Graph;
import com.blstream.myguide.zoolocations.Animal;
import com.blstream.myguide.zoolocations.Language;
import com.blstream.myguide.zoolocations.Node;
import com.blstream.myguide.zoolocations.Track;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Class which draw chosen track on the Google Map. It also mark animals with
 * different markers. When new track is about to be drawn, old one won't be
 * display anymore.
 */
public class TrackDrawer {
	private static final float TRACK_WIDTH = 8.5f;
	private static final int POLYLINE_ZINDEX = 3;
	private static final int TRACK_PADDING = 60;
	private static final double MARKER_ACCURACY = 0.0000002;

	private Graph mGraph;
	private Polyline mCurrentTrackPolyline;
	private ArrayList<Marker> mAnimalMarkers;
	private ArrayList<Marker> mAnimalOnTrackMarkers;
	private GoogleMap mMap;

	public TrackDrawer(Graph graph, GoogleMap map, ArrayList<Marker> animalMarkers) {
		mGraph = graph;
		mAnimalMarkers = animalMarkers;
		mAnimalOnTrackMarkers = new ArrayList<Marker>();
		mMap = map;
	}

	/**
	 * Draw a given track on the given map and zoom map to cover whole track
	 * 
	 * @param track to be drawn
	 * @param color of a track
	 * @param pathsVisible if false, Polyline will be invisible
	 * @return object of Polyline drawn on map which represent track
	 */
	public Polyline drawTrack(Track track, int color, boolean pathsVisible) {
		cleanTrack();
		PolylineOptions polylineOptions = new PolylineOptions().width(TRACK_WIDTH)
				.color(color)
				.zIndex(POLYLINE_ZINDEX);
		final LatLngBounds.Builder trackBoundsBuilder = LatLngBounds.builder();

		ArrayList<Node> path = generatePathBetweenAnimals(track);
		for (Node node : path) {
			LatLng position = new LatLng(node.getLatitude(), node.getLongitude());
			trackBoundsBuilder.include(position);
			polylineOptions.add(position);
		}

		mCurrentTrackPolyline = mMap.addPolyline(polylineOptions);
		mCurrentTrackPolyline.setVisible(pathsVisible);
		markAnimalsOnTrack(track, trackBoundsBuilder);

		mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
			@Override
			public void onCameraChange(CameraPosition cameraPosition) {
				mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(trackBoundsBuilder.build(),
						TRACK_PADDING));
				// Remove listener to prevent position reset on camera move.
				mMap.setOnCameraChangeListener(null);
			}
		});

		return mCurrentTrackPolyline;
	}

	/**
	 * Remove track drawn before on the map.
	 */
	public void cleanTrack() {
		if (mCurrentTrackPolyline != null) {
			mCurrentTrackPolyline.remove();
			for (Marker m : mAnimalOnTrackMarkers) {
				m.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_animal));
			}
			for (Marker m : mAnimalMarkers) {
				m.setAlpha(1.0f);
			}
		}
	}

	private void markAnimalsOnTrack(Track track, LatLngBounds.Builder trackBoundsBuilder) {
		mAnimalOnTrackMarkers.clear();
		for (Marker m : mAnimalMarkers) {
			m.setAlpha(0.5f);
		}
		for (Animal a : track.getAnimals()) {
			trackBoundsBuilder
					.include(new LatLng(a.getNode().getLatitude(), a.getNode().getLongitude()));
			for (Marker m : mAnimalMarkers) {
				if (isMarkerRepresentsAnimal(m, a)) {
					m.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_animal_on_track));
					m.setAlpha(1.0f);
					mAnimalOnTrackMarkers.add(m);
				}
			}
		}
	}

	private ArrayList<Node> generatePathBetweenAnimals(Track track) {
		ArrayList<Node> path = new ArrayList<Node>();
		ListIterator<Animal> iterator = track.getAnimals().listIterator();
		Animal previousAnimal = iterator.next();
		Animal currentAnimal;
		while (iterator.hasNext()) {
			currentAnimal = iterator.next();
			path.addAll(mGraph.findPath(previousAnimal.getNode(), currentAnimal.getNode()));
			previousAnimal = currentAnimal;
		}
		path.addAll(mGraph.findPath(previousAnimal.getNode(), track.getAnimals().get(0).getNode()));
		return path;
	}
	
	private boolean isMarkerRepresentsAnimal(Marker m, Animal a) {
		/*
		 * In Google Maps API v2 Markers don't have extra field to store
		 * additional data, so Animal ID can not be stored inside it. To check
		 * if Marker correspond to Animal their location can be compare.
		 * MARKER_ACCURACY must be used to compensate small inaccuracy, become
		 * Markers are not in exactly the same location as Animal.
		 */
		if (a.getNode().getLatitude() + MARKER_ACCURACY > m.getPosition().latitude
				&& a.getNode().getLatitude() - MARKER_ACCURACY < m.getPosition().latitude
				&& a.getNode().getLongitude() + MARKER_ACCURACY > m.getPosition().longitude
				&& a.getNode().getLongitude() - MARKER_ACCURACY < m.getPosition().longitude) return true;
		else return false;
	}
}

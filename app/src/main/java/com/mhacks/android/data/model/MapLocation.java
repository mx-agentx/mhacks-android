package com.mhacks.android.data.model;

import android.os.Parcel;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.mhacks.android.data.sync.Synchronize;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.List;

/**
 * Created by Damian Wieczorek <damianw@umich.edu> on 8/2/14.
 */
@ParseClassName(MapLocation.CLASS)
public class MapLocation extends DataClass<MapLocation> {
  public static final String CLASS = "MapLocation";

  public static final String TITLE = "title";
  public static final String DETAILS = "details";
  public static final String IMAGE = "image";
  public static final String COLOR = "color";
  public static final String LOCATION = "location";
  public static final String BOUNDS = "bounds";

  public MapLocation() {
    super(false);
  }

  public MapLocation(String title, String details, ParseGeoPoint location) {
    super(true);

    setTitle(title);
    setDetails(details);
    setLocation(location);
  }

  public String getTitle() {
    return getString(TITLE);
  }

  public MapLocation setTitle(String title) {
    return builderPut(TITLE, title);
  }

  public String getDetails() {
    return getString(DETAILS);
  }

  public MapLocation setDetails(String details) {
    return builderPut(DETAILS, details);
  }

  public ParseFile getImageFile() {
    return getParseFile(IMAGE);
  }

  public MapLocation setImageFile(ParseFile file) {
    return builderPut(IMAGE, file);
  }

  public int getColor() {
    return getInt(COLOR);
  }

  public MapLocation setColor(int color) {
    return builderPut(COLOR, color);
  }

  public LatLng getLocation() {
    ParseGeoPoint point = getParseGeoPoint(LOCATION);
    return new LatLng(point.getLatitude(), point.getLongitude());
  }

  public MapLocation setLocation(ParseGeoPoint location) {
    return builderPut(LOCATION, location);
  }

  public LatLngBounds getBounds() {
    List<ParseGeoPoint> points = getList(BOUNDS);
    LatLngBounds.Builder builder = LatLngBounds.builder();
    for (ParseGeoPoint point : points) {
      builder.include(new LatLng(point.getLatitude(), point.getLongitude()));
    }
    return builder.build();
  }

  public List<LatLng> getGeometry() {
    return Lists.transform(this.<ParseGeoPoint>getList(BOUNDS), new Function<ParseGeoPoint, LatLng>() {
      @Override
      public LatLng apply(ParseGeoPoint input) {
        return new LatLng(input.getLatitude() - 1e-5, input.getLongitude() + 8e-5);
      }
    });
  }

  public MapLocation setGeometry(List<ParseGeoPoint> bounds) {
    return builderPut(BOUNDS, bounds);
  }

  public static ParseQuery<MapLocation> query() {
    return ParseQuery.getQuery(MapLocation.class).fromLocalDatastore();
  }

  public static ParseQuery<MapLocation> remoteQuery() {
    return ParseQuery.getQuery(MapLocation.class);
  }

  public static final Creator<MapLocation> CREATOR = new Creator<MapLocation>() {
    @Override
    public MapLocation createFromParcel(Parcel parcel) {
      try {
        return query().fromLocalDatastore().get(parcel.readString());
      } catch (ParseException e) {
        e.printStackTrace();
      }
      return null;
    }

    @Override
    public MapLocation[] newArray(int i) {
      return new MapLocation[0];
    }
  };

  public static Synchronize<MapLocation> getSync() {
    return new Synchronize<>(new ParseQueryAdapter.QueryFactory<MapLocation>() {
      @Override
      public ParseQuery<MapLocation> create() {
        return remoteQuery();
      }
    });
  }

}

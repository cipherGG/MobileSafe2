package com.gg.app.mobilesafe2.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import com.gg.app.mobilesafe2.utils.PreferenceUtils;

public class LocationService extends Service {

    private LocationManager locationManager;

    public LocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        getLocation();
    }

    private void getLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // List<String> allProviders = locationManager.getAllProviders();

        Criteria criteria = new Criteria();
        criteria.setCostAllowed(true);
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String bestProvider = locationManager.getBestProvider(criteria, true);

        // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
        locationManager.requestLocationUpdates(bestProvider, 0, 0, mLocationListener);
    }

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            try {
                //转化火星坐标为标准坐标
                ModifyOffset instance = ModifyOffset.getInstance(ModifyOffset.class.getResourceAsStream("axisoffset.dat"));
                PointDouble pointDouble = instance.s2c(new PointDouble(location.getLongitude(), location.getLatitude()));
                //存到内存中，供下次查询
                PreferenceUtils.putString(LocationService.this, "location", "经度:" + pointDouble.x + "\n纬度:" + pointDouble.y);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                //记得关闭服务
                stopSelf();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        //耗电量很大的，不用的话就关掉吧
        locationManager.removeUpdates(mLocationListener);
    }
}

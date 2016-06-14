package com.botconf.android

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.view.View
import com.botconf.R
import groovy.transform.CompileStatic

@CompileStatic
trait TraitOpenGoogleMaps {

    abstract def getPackageManager()

    void openMapAtLocation(PackageManager packageManager, float latitude, float longitude, String name) {
        // Create a Uri from an intent string. Use the result to create an Intent.
        Uri gmmIntentUri = Uri.parse("geo:${latitude},${longitude}?q=" + Uri.encode("${name}"));
        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        // Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps");

        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent);
        } else {
            showSnackBar(anchorView(),R.string.snackbar_install_a_maps_application, Snackbar.LENGTH_LONG,R.color.snackbar_red)
        }
    }

    void openMapWithDirectionsToLocation(float latitude, float longitude, String name) {
        String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?&daddr=%f,%f (%s)", latitude, longitude, name);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        try  {
            startActivity(intent);
        } catch(ActivityNotFoundException ex) {
            try {
                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(unrestrictedIntent);
            } catch(ActivityNotFoundException innerEx) {
                showSnackBar(anchorView(),R.string.snackbar_install_a_maps_application, Snackbar.LENGTH_LONG,R.color.snackbar_red)
            }
        }
    }



    abstract View anchorView()

    abstract void startActivity(Intent intent)
    abstract void showSnackBar(View v, int stringResource, int length, int colorResource)
}
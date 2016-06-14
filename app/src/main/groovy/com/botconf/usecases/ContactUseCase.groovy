package com.botconf.usecases

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import groovy.transform.CompileStatic

@CompileStatic
class ContactUseCase {

    static void startPhoneIntent(Context context, String phoneNumber) {
        Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber))
        context.startActivity(i);
    }

    static void startBrowserIntent(Context context, String url) throws ActivityNotFoundException {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(browserIntent)
    }

    static void startEmailIntent(Context context, String email, String subject) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto",email, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        context.startActivity(Intent.createChooser(emailIntent, "Send Email..."))
    }

    static void startLocationIntent(Context context, double latitude, double longitude, String title) {
        String url = "geo:${latitude},${longitude}?q=${title}"
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }

}
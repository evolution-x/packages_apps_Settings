/*
 * Copyright (C) 2019-2021 The Evolution X Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.deviceinfo.firmwareversion;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.preference.Preference;

import com.android.settings.R;
import com.android.settings.core.BasePreferenceController;

public class EvolutionXVersionPreferenceController extends BasePreferenceController {

    private static final Uri INTENT_URI_DATA = Uri.parse("https://paypal.me/joeyhuab/");
    private static final String TAG = "EvolutionXVersionPreferenceCtrl";
    private static final String KEY_ROM_VERSION_PROP = "org.evolution.build_version";
    private static final String KEY_ROM_CODENAME_PROP = "org.evolution.build_codename";
    private static final String KEY_ROM_BUILD_TYPE_PROP = "org.evolution.build_type";

    private final PackageManager mPackageManager;

    public EvolutionXVersionPreferenceController(Context context, String key) {
        super(context, key);
        mPackageManager = mContext.getPackageManager();
    }

    @Override
    public int getAvailabilityStatus() {
        return AVAILABLE;
    }

    @Override
    public CharSequence getSummary() {
        String romVersion = SystemProperties.get(KEY_ROM_VERSION_PROP,
                mContext.getString(R.string.device_info_default));
        String romCodename = SystemProperties.get(KEY_ROM_CODENAME_PROP,
                mContext.getString(R.string.device_info_default));
        String romBuildtype = SystemProperties.get(KEY_ROM_BUILD_TYPE_PROP,
                mContext.getString(R.string.device_info_default));
        return romVersion + " " + romCodename + " | " + romBuildtype;
    }

    @Override
    public boolean handlePreferenceTreeClick(Preference preference) {
        if (!TextUtils.equals(preference.getKey(), getPreferenceKey())) {
            return false;
        }

        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(INTENT_URI_DATA);
        if (mPackageManager.queryIntentActivities(intent, 0).isEmpty()) {
            // Don't send out the intent to stop crash
            Log.w(TAG, "queryIntentActivities() returns empty");
            return true;
        }

        mContext.startActivity(intent);
        return true;
    }
}

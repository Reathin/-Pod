package com.rair.pod.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Activity跳转封装
 *
 * @author Rairmmd
 * @date 2017-10-17
 */
public class XRouter {

    private Intent intent;
    private Activity from;
    private Class<?> to;
    private Bundle data;
    private ActivityOptionsCompat options;
    private int requestCode = -1;
    private int enterAnim = RES_NONE;
    private int exitAnim = RES_NONE;

    public static final int RES_NONE = -1;

    private XRouter() {
        intent = new Intent();
    }

    public static XRouter newIntent(Activity context) {
        XRouter router = new XRouter();
        router.from = context;
        return router;
    }

    public XRouter to(Class<?> to) {
        this.to = to;
        return this;
    }

    public XRouter addFlags(int flags) {
        if (intent != null) {
            intent.addFlags(flags);
        }
        return this;
    }

    public XRouter data(Bundle data) {
        this.data = data;
        return this;
    }

    public XRouter putByte(@Nullable String key, byte value) {
        getBundleData().putByte(key, value);
        return this;
    }

    public XRouter putChar(@Nullable String key, char value) {
        getBundleData().putChar(key, value);
        return this;
    }

    public XRouter putInt(@Nullable String key, int value) {
        getBundleData().putInt(key, value);
        return this;
    }

    public XRouter putLong(@Nullable String key, long value) {
        getBundleData().putLong(key, value);
        return this;
    }

    public XRouter putString(@Nullable String key, String value) {
        getBundleData().putString(key, value);
        return this;
    }

    public XRouter putShort(@Nullable String key, short value) {
        getBundleData().putShort(key, value);
        return this;
    }

    public XRouter putFloat(@Nullable String key, float value) {
        getBundleData().putFloat(key, value);
        return this;
    }

    public XRouter putCharSequence(@Nullable String key, @Nullable CharSequence value) {
        getBundleData().putCharSequence(key, value);
        return this;
    }

    public XRouter putParcelable(@Nullable String key, @Nullable Parcelable value) {
        getBundleData().putParcelable(key, value);
        return this;
    }

    public XRouter putParcelableArray(@Nullable String key, @Nullable Parcelable[] value) {
        getBundleData().putParcelableArray(key, value);
        return this;
    }

    public XRouter putParcelableArrayList(@Nullable String key,
                                          @Nullable ArrayList<? extends Parcelable> value) {
        getBundleData().putParcelableArrayList(key, value);
        return this;
    }


    public XRouter putIntegerArrayList(@Nullable String key, @Nullable ArrayList<Integer> value) {
        getBundleData().putIntegerArrayList(key, value);
        return this;
    }

    public XRouter putStringArrayList(@Nullable String key, @Nullable ArrayList<String> value) {
        getBundleData().putStringArrayList(key, value);
        return this;
    }

    public XRouter putCharSequenceArrayList(@Nullable String key,
                                            @Nullable ArrayList<CharSequence> value) {
        getBundleData().putCharSequenceArrayList(key, value);
        return this;
    }

    public XRouter putSerializable(@Nullable String key, @Nullable Serializable value) {
        getBundleData().putSerializable(key, value);
        return this;
    }


    public XRouter options(ActivityOptionsCompat options) {
        this.options = options;
        return this;
    }

    public XRouter requestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    public XRouter anim(int enterAnim, int exitAnim) {
        this.enterAnim = enterAnim;
        this.exitAnim = exitAnim;
        return this;
    }

    public void launch() {
        if (intent != null && from != null && to != null) {
            intent.setClass(from, to);
            intent.putExtras(getBundleData());
            if (options == null) {
                if (requestCode < 0) {
                    from.startActivity(intent);
                } else {
                    from.startActivityForResult(intent, requestCode);
                }
                if (enterAnim > 0 && exitAnim > 0) {
                    from.overridePendingTransition(enterAnim, exitAnim);
                }
            } else {
                if (requestCode < 0) {
                    ActivityCompat.startActivity(from, intent, options.toBundle());
                } else {
                    ActivityCompat.startActivityForResult(from, intent, requestCode, options.toBundle());
                }
            }
        }
    }

    private Bundle getBundleData() {
        if (data == null) {
            data = new Bundle();
        }
        return data;
    }

    public static void pop(Activity activity) {
        activity.finish();
    }

}

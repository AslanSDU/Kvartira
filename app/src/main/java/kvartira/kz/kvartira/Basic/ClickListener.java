package kvartira.kz.kvartira.Basic;

import android.view.View;

/**
 * Created by Aslan on 08.04.2016.
 */
public interface ClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}

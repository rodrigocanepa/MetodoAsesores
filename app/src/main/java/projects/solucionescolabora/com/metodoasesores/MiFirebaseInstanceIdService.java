package projects.solucionescolabora.com.metodoasesores;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by rodrigocanepacruz on 06/11/18.
 */

public class MiFirebaseInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String token = FirebaseInstanceId.getInstance().getToken();

        Log.i("NOTIFICACIONES", "token" + token);
    }
}

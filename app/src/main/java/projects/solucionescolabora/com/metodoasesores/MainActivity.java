package projects.solucionescolabora.com.metodoasesores;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import projects.solucionescolabora.com.metodoasesores.Fragments.AvancesFragment;
import projects.solucionescolabora.com.metodoasesores.Fragments.ConsultoresFragment;
import projects.solucionescolabora.com.metodoasesores.Fragments.CursosFragment;
import projects.solucionescolabora.com.metodoasesores.Fragments.DiagnosticoFragment;
import projects.solucionescolabora.com.metodoasesores.Fragments.HomeFragment;
import projects.solucionescolabora.com.metodoasesores.Fragments.PerfilFragment;
import projects.solucionescolabora.com.metodoasesores.Modelos.FirebaseReferences;
import projects.solucionescolabora.com.metodoasesores.Modelos.Startup;
import projects.solucionescolabora.com.metodoasesores.Startups.RegistroStartupActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.OnFragmentInteractionListener, AvancesFragment.OnFragmentInteractionListener, ConsultoresFragment.OnFragmentInteractionListener,
        CursosFragment.OnFragmentInteractionListener, DiagnosticoFragment.OnFragmentInteractionListener, PerfilFragment.OnFragmentInteractionListener {

    boolean FragmentTransaction;
    Fragment fragment;
    Fragment initialFragment;
    private SharedPreferences sharedPreferences;

    private int auxiliar = 0;
    public NavigationView navigationView;

    // ***************** FRAGMENT ***************************************
    private CircleImageView imgAvatar;
    private TextView txtNombreStartup;
    private TextView txtNombreEmprededor;

    private String idActual = "";
    private String nombreActual = "";
    private String especialidadActual = "";
    private String urlFotoActual = "";
    private boolean saveFoto = false;
    private String correoActual = "";

    private String url = "";
    private String startupNombre = "";
    private String nombre = "";
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private String UUIDUser = "";

    // ******************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        // navigationView.setBackground(getDrawable(R.drawable.side_nav_bar));
        // navigationView.setBackgroundColor(Color.argb(200,15,15,15));
        navigationView.setNavigationItemSelectedListener(this);

        View hView =  navigationView.getHeaderView(0);
        imgAvatar = (CircleImageView) hView.findViewById(R.id.imageViewNavBar);
        txtNombreEmprededor = (TextView)hView.findViewById(R.id.txtNavEspecialidad);
        txtNombreStartup = (TextView) hView.findViewById(R.id.txtNavNombre);

        sharedPreferences = getSharedPreferences("misDatos", 0);
        idActual = sharedPreferences.getString("idActual","");
        nombreActual = sharedPreferences.getString("nombreActual","");
        especialidadActual = sharedPreferences.getString("especialidadActual","");
        urlFotoActual = sharedPreferences.getString("urlFotoActual","");
        saveFoto = sharedPreferences.getBoolean("saveFoto",false);

        if (idActual.length() > 1){
            txtNombreStartup.setText(especialidadActual);
            txtNombreEmprededor.setText(nombreActual);

            if (saveFoto){
                File folder = new  File(Environment.getExternalStorageDirectory().toString(), "MetodoAsesoresImagenes");
                if(!folder.exists())
                    folder.mkdirs();
                File file = new File(folder, "fotoMetodoApp.png");

                if(file.exists()){

                    Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

                    imgAvatar.setImageBitmap(myBitmap);
                    //imgFondo.setImageBitmap(myBitmap);
                    //imgFondo.setColorFilter(Color.argb(180,7,45,74), PorterDuff.Mode.DARKEN);
                    saveFoto = false;
                    initialFragment = new HomeFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.content_main, initialFragment).commit();
                    getSupportActionBar().setTitle("Inicio");
                }
            }
            else{
                // ********* IMPORTANTE CAMBIAR ************
                StorageReference storageRef = storage.getReferenceFromUrl("gs://metodo-asesores.appspot.com/Usuarios/Startup").child(idActual + "-profile");
                final long ONE_MEGABYTE = 1024 * 1024;
                storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        imgAvatar.setImageBitmap(bitmap);
                      //  imgFondo.setImageBitmap(bitmap);
                      //  imgFondo.setColorFilter(Color.argb(180,7,45,74), PorterDuff.Mode.DARKEN);

                        // ***************************** GUARDAMOS LA IMAGEN ***********************
                        File folder = new File(Environment.getExternalStorageDirectory().toString(), "MetodoAsesoresImagenes");

                        if(!folder.exists())
                            folder.mkdirs();
                        File file = new File(folder, "fotoMetodoApp.png");

                        if (file.exists ()) file.delete ();
                        try {
                            FileOutputStream out = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                            out.flush();
                            out.close();

                            // *********** Guardamos los principales datos de los nuevos usuarios *************
                            sharedPreferences = getSharedPreferences("misDatos", 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("saveFoto", true);
                            editor.commit();
                            initialFragment = new HomeFragment();
                            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, initialFragment).commit();
                            getSupportActionBar().setTitle("Inicio");
                            // ******************************************************************************

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                    }
                });
            }

        }
        else{
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //FirebaseAuth.getInstance().getCurrentUser;
            UUIDUser = user.getUid();
            idActual = UUIDUser;
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference tutorialRef = database.getReference(FirebaseReferences.DATABASE_REFERENCE);

            tutorialRef.child(FirebaseReferences.USUARIOS_REFERENCE).child(FirebaseReferences.STARTUP_REFERENCE).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    // ******** OBTENEMOS TODOS LOS ELEMENTOS **********
                    for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        final Startup startup = snapshot.getValue(Startup.class);

                        if(startup.getId().equals(UUIDUser)){
                            url = startup.getUrlFoto();
                            nombre = startup.getNombres() + " " + startup.getApellidos();
                            startupNombre = startup.getNombreStartup();
                        }

                    }

                    if(url.length() > 2){
                        // ********* IMPORTANTE CAMBIAR ************
                        StorageReference storageRef = storage.getReferenceFromUrl("gs://metodo-asesores.appspot.com/Usuarios/Startup").child(UUIDUser + "-profile");
                        final long ONE_MEGABYTE = 1024 * 1024;
                        storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                imgAvatar.setImageBitmap(bitmap);
                                //imgFondo.setImageBitmap(bitmap);
                                //imgFondo.setColorFilter(Color.argb(180,7,45,74), PorterDuff.Mode.DARKEN);

                                // ***************************** GUARDAMOS LA IMAGEN ***********************
                                File folder = new File(Environment.getExternalStorageDirectory().toString(), "MetodoAsesoresImagenes");

                                if(!folder.exists())
                                    folder.mkdirs();
                                File file = new File(folder, "fotoMetodoApp.png");

                                if (file.exists ()) file.delete ();
                                try {
                                    FileOutputStream out = new FileOutputStream(file);
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                                    out.flush();
                                    out.close();

                                    // *********** Guardamos los principales datos de los nuevos usuarios *************
                                    sharedPreferences = getSharedPreferences("misDatos", 0);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean("saveFoto", true);
                                    editor.commit();
                                    // ******************************************************************************

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                // *********** Guardamos los principales datos de los nuevos usuarios *************
                                sharedPreferences = getSharedPreferences("misDatos", 0);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("idActual", UUIDUser);
                                editor.putString("nombreActual", nombre);
                                editor.putString("especialidadActual", startupNombre);
                                editor.putString("urlFotoActual", url);
                                editor.putString("correoActual", correoActual);

                                idActual = UUIDUser;
                                nombreActual = nombre;
                                especialidadActual = startupNombre;
                                urlFotoActual = url;
                                editor.commit();

                                initialFragment = new HomeFragment();
                                getSupportFragmentManager().beginTransaction().replace(R.id.content_main, initialFragment).commit();
                                getSupportActionBar().setTitle("Inicio");
                                // ******************************************************************************

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                            }
                        });
                    }

                    if(nombre.length() > 1){
                        txtNombreEmprededor.setText(nombre);
                    }
                    if(startupNombre.length() > 1){
                        txtNombreStartup.setText(startupNombre);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();

            if(auxiliar == 0){
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Salir");
                builder.setMessage("¿Deseas salir de la aplicación?")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                // Create the AlertDialog object and return it
                builder.create();
                builder.show();
            }
            else{
                initialFragment = new HomeFragment();
                auxiliar = 0;
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main, initialFragment).commit();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentTransaction = false;
        fragment = null;

        if (id == R.id.nav_home) {
            // Handle the camera action
            fragment = new HomeFragment();
            FragmentTransaction = true;
            auxiliar = 0;

        } else if (id == R.id.nav_diagnostico) {

            fragment = new DiagnosticoFragment();
            FragmentTransaction = true;
            auxiliar = 1;

        } else if (id == R.id.nav_consultores) {

            fragment = new ConsultoresFragment();
            FragmentTransaction = true;
            auxiliar = 1;


        } else if (id == R.id.nav_cursos) {

            fragment = new CursosFragment();
            FragmentTransaction = true;
            auxiliar = 1;


        } else if (id == R.id.nav_modificar_perfil) {

            Intent i = new Intent(MainActivity.this, RegistroStartupActivity.class);
            startActivity(i);
            /*fragment = new PerfilFragment();
            FragmentTransaction = true;
            auxiliar = 1;*/


        }
        /*else if (id == R.id.nav_avances) {

            fragment = new AvancesFragment();
            FragmentTransaction = true;
            auxiliar = 1;


        }*/
         else if (id == R.id.nav_cerrar_sesion) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Cerrar sesión");
            builder.setMessage("¿Estas seguro de querer cerrar sesión?")
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            FirebaseAuth.getInstance().signOut();
                            LoginManager.getInstance().logOut();
                            // *********** Guardamos los principales datos de los nuevos usuarios *************
                            sharedPreferences = getSharedPreferences("misDatos", 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("typeCurrentUser", "");
                            editor.putString("registroCompleto", "");
                            editor.putString("idActual", "");
                            editor.putString("nombreActual", "");
                            editor.putString("especialidadActual", "");
                            editor.putString("urlFotoActual", "");
                            editor.putString("correoActual", "");
                            editor.commit();
                            // ******************************************************************************
                            Intent i = new Intent(MainActivity.this, LogOrRegActivity.class);
                            startActivity(i);
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
            // Create the AlertDialog object and return it
            builder.create();
            builder.show();
        }

        if (FragmentTransaction){

            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment).commit();

            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

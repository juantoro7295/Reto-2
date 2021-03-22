package co.com.registropersonamovil2021;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.com.registropersonamovil2021.entity.Persona;
import co.com.registropersonamovil2021.persistencia.Connection;
import co.com.registropersonamovil2021.util.ActionBarUtil;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class EditarPersonaActivity extends AppCompatActivity {

    @BindView(R.id.editar_documento)
    TextView editarDocumento;

    @BindView(R.id.editar_nombre)
    EditText editarNombre;

    @BindView(R.id.editar_apellido)
    EditText editarApellido;


    Persona persona;
    Integer idPersona;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_persona);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        ActionBarUtil.getInstance(this, true).setToolBar(getString(R.string.editar_persona), getString(R.string.editar));
        persona = new Persona();
        idPersona= intent.getIntExtra("id_persona",0);
        editarDocumento.setText(intent.getStringExtra("documento_persona"));
        editarNombre.setText(intent.getStringExtra("nombre_persona"));
        editarApellido.setText(intent.getStringExtra("apellido_persona"));

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save) {
            cargarInformacion();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void cargarInformacion() {

        persona.setIdPersona(idPersona);
        persona.setNumeroDocumentoIdentidad(editarDocumento.getText().toString());
        persona.setNombrePersona(editarNombre.getText().toString());
        persona.setApellidoPersona(editarApellido.getText().toString());
        dialogoDeConfirmacionEditarRegistro();


    }
    private void dialogoDeConfirmacionEditarRegistro(){
        AlertDialog.Builder builder = new AlertDialog.Builder(EditarPersonaActivity.this);
        builder.setCancelable(false);
        builder.setTitle(R.string.confirm);
        builder.setMessage(R.string.confirm_message_guardar_informacion);
        builder.setPositiveButton(R.string.confirm_action, (dialog, which) -> modificarInformacionPersona());
        builder.setNegativeButton(R.string.cancelar, (dialog, which) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void modificarInformacionPersona() {
        Observable.fromCallable(()-> {
            Connection.getDb(getApplicationContext()).getPersonaDao().update(persona);
            finish();
            return persona;
        }).subscribeOn(Schedulers.computation()).subscribe();
    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
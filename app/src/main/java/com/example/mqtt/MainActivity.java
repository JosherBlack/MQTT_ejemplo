package com.example.mqtt;

import androidx.appcompat.app.AppCompatActivity;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    //Datos Configuracion de MQTT  DIRECCION DE SERVER, USUARIO y CONTRASEÑA
    public static String MQTTHOST = ""; // debe comenzar con "tcp://direcion:1883" (datos de su servidor)
    public static String USERNAME = ""; // usurio
    public static String PASSWORD = ""; // contraseña

    MqttAndroidClient client; // sea crea el nuevo cliente

    public TextView TV;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TV = (TextView)findViewById(R.id.TV);

        //MQTT conexión

        String clientID = MqttClient.generateClientId();
        client = new MqttAndroidClient(getApplicationContext(), MQTTHOST, clientID);
        final MqttConnectOptions options = new MqttConnectOptions();

        //options.setUserName(USERNAME); // Descomentar si existe usario
        //options.setPassword(PASSWORD.toCharArray()); // Descomentar si existe contraseña


        // TRY/CATCH de conexión
        try {
            IMqttToken token = client.connect(options); //se crea un nuevo token
            token.setActionCallback(new IMqttActionListener() { //accion escuchadora del token
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // Estamos Conecatados
                    Toast.makeText(getBaseContext(), "CONECTADO", Toast.LENGTH_LONG).show();
                    subscribetopics(); // se inicializan los topicos seleccionados
                }
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Si pasan problemas
                    Toast.makeText(getBaseContext(), "ERROR", Toast.LENGTH_LONG).show();
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        // Metodos CALLBACK DE MQTT

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {

                if(topic.matches("TOPICO_MUESTRA")){ // SI EL TOPICO ES "TANTO" mostrar los dartos
                    TV.setText(new String(message.getPayload())); //LE DIGHO QUE EL DATO SE MUESTRE EN EL TextVieW
                }
        }
            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                //EN CASO DE MANDAR DATOS TRABAJAR AQUI
            }
        });
    }

    private void subscribetopics()
    {
        try{
            client.subscribe("TOPICO_MUESTRA", 0); //SUBSCRIBIENDO TOPICO DE MUESTRA
        }
        catch(MqttException e)
        {
            e.printStackTrace();
        }
    }
}
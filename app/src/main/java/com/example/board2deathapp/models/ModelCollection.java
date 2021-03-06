package com.example.board2deathapp.models;

import android.util.Log;
import android.view.Display;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class ModelCollection<T extends Model> {

    private List<Model> data;
    private Class clazz;
    public ModelCollection(Class clazz){
        this.clazz = clazz;
        this.data = new ArrayList<>();

    }

    public List getItems(){
        return this.data;
    }
    public void read(final Query q, final DBResponse dbResponse){
        q.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d(this.getClass().getName().toUpperCase(), "Successfully acquired" + q);
                    ModelCollection.this.data.clear();
                    List<DocumentSnapshot> t = task.getResult().getDocuments();
                    for(DocumentSnapshot doc:t){
                        Model m;
                        try{
                            m =(Model) clazz.newInstance();
                        }catch(Exception error){
                            m= (Model) doc.toObject(clazz);
                        }

                        m.fromMap(doc.getData());
                        m.setID(doc.getReference().getId());
                        data.add(m);
                    }
                    if(dbResponse != null){
                        dbResponse.onSuccess(task.getResult().getDocuments());
                    }

                } else {
                    Log.d(this.getClass().getName().toUpperCase(), "Failed to get" + q);
                    if(dbResponse != null) {
                        dbResponse.onFailure(null);
                    }
                }
            }
        });
    }

    public void read_current(final Query q, final DBResponse dbResponse){
        q.addSnapshotListener(
                new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        data.clear();
                        List<DocumentSnapshot> t = queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot doc:t){
                            Model m;
                            try{
                                m =(Model) clazz.newInstance();
                            }catch(Exception error){
                                m= (Model) doc.toObject(clazz);
                            }

                            m.fromMap(doc.getData());
                            m.setID(doc.getReference().getId());
                            data.add(m);
                        }
                        if(dbResponse != null){
                            dbResponse.onSuccess(queryDocumentSnapshots);
                        }
                    }
                }
        );
    }
}

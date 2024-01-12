package com.example.droidtools.ShowUplodedDataFromDatabase;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.droidtools.FileUploadOnServer.FileUploader;
import com.example.droidtools.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterForDBS extends RecyclerView.Adapter<AdapterForDBS.PdfViewHolder> {

    interface pdfClickListner {
         void pdfClick(String url);
    }

    private pdfClickListner listner;
    private Context context;
    private List<FileUploader> pdfFiles;


    public AdapterForDBS(Context context) {
        this.context = context;
        this.pdfFiles = new ArrayList<>();
        listner = (pdfClickListner) context;
    }



    @NonNull
    @Override
    public PdfViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_for_pdf_from_dbs, parent, false);
        return new PdfViewHolder(view,listner);
    }

    @Override
    public void onBindViewHolder(@NonNull PdfViewHolder holder, int position) {
        holder.title.setText(pdfFiles.get(position).getName());

    }

    public void setData(ArrayList<FileUploader> pdfList) {
        this.pdfFiles = pdfList;
    }

    @Override
    public int getItemCount() {
        return pdfFiles.size();
    }

    class PdfViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title;
        public ImageView pdfImage;
        public pdfClickListner listner;
        public PdfViewHolder(@NonNull View itemView,@NonNull pdfClickListner listner) {
            super(itemView);
            this.listner=listner;
            title = itemView.findViewById(R.id.pdf_title);
            pdfImage = itemView.findViewById(R.id.pdf_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String url = pdfFiles.get(getAdapterPosition()).getUrl();
            listner.pdfClick(url);
        }
    }
}
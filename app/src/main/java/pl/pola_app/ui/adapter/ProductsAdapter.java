package pl.pola_app.ui.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.pola_app.R;
import pl.pola_app.model.SearchResult;
import timber.log.Timber;

public class ProductsAdapter extends android.support.v7.widget.RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    public interface ProductClickListener {
        void itemClicked(SearchResult searchResult);
    }

    @NonNull private final Context context;
    @NonNull private final ProductList searchResults;
    @Nullable private ProductClickListener productClickListener;

    public ProductsAdapter(@NonNull final Context context,
                           @NonNull final ProductList searchResults) {
        this.searchResults = searchResults;
        this.context = context;
    }

    public void setOnProductClickListener(ProductClickListener productClickListener) {
        this.productClickListener = productClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_product_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final SearchResult p = searchResults.get(i);
        viewHolder.bind(p);
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    private void onItemClicked(int position) {
        if (productClickListener == null) {
            return;
        }

        if (position > searchResults.size()) { //TODO: is it even possible to reach this state?
            Timber.w(new IndexOutOfBoundsException(), "Position: %d, list size: %d", position, searchResults.size());
            return;
        }

        SearchResult searchResult = searchResults.get(position);
        if (searchResult != null) {
            productClickListener.itemClicked(searchResult);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @Bind(R.id.company_name)
        TextView companyName;

        @Bind(R.id.score_bar)
        ProgressBar plScore;

        @Bind(R.id.view_product_item)
        CardView productCard;

        @Bind(R.id.progressBar)
        ProgressBar progress;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void bind(SearchResult searchResult) {
            if (searchResult == null) {
                progress.setVisibility(View.VISIBLE);
                companyName.setText("");
                plScore.setProgress(0);
                applyStyle(context.getString(R.string.type_white));
                return;
            }

            progress.setVisibility(View.GONE);

            applyStyle(searchResult.card_type);
            companyName.setText(searchResult.name);

            if (searchResult.plScore != null) {
                plScore.setProgress(searchResult.plScore);
            } else {
                plScore.setProgress(0);
            }
        }

        private void applyStyle(String style) {
            final Resources resources = context.getResources();
            if (style.equals(resources.getString(R.string.type_grey))) {
                productCard.setCardBackgroundColor(resources.getColor(R.color.card_type_grey_bk));
                plScore.setBackgroundColor(resources.getColor(R.color.card_type_grey_score_bk));
            } else {
                productCard.setCardBackgroundColor(resources.getColor(R.color.card_type_white_bk));
                plScore.setBackgroundColor(resources.getColor(R.color.card_type_white_score_bk));
            }
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                onItemClicked(position);
            }
        }
    }
}

package kg.geektech.posttapp.utils.interfaces;

import kg.geektech.posttapp.data.models.PostModel;

public interface OnClick {
    void onClick(PostModel postModel);
    void onLongClick(PostModel postModel);
}

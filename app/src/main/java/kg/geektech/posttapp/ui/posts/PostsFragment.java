package kg.geektech.posttapp.ui.posts;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.HashMap;
import java.util.List;

import kg.geektech.posttapp.R;
import kg.geektech.posttapp.data.models.PostModel;
import kg.geektech.posttapp.databinding.FragmentPostsBinding;
import kg.geektech.posttapp.ui.form.FormFragment;
import kg.geektech.posttapp.utils.app.App;
import kg.geektech.posttapp.utils.interfaces.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PostsFragment extends Fragment implements OnClick {
    private FragmentPostsBinding binding;
    private PostAdapter adapter;
    private NavController navController;
    private HashMap<Integer, String> hashMap = new HashMap<>();

    public PostsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new PostAdapter(this);
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPostsBinding.inflate(LayoutInflater.from(getContext()), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getPostApi();
        hashMapUser();
        binding.recyclerPosts.setAdapter(adapter);
        binding.fab.setOnClickListener(view1 -> {
            navController.navigate(R.id.formFragment);
        });
    }

    private void hashMapUser() {
        hashMap.put(0, "noName");
        hashMap.put(1, "User 1");
        hashMap.put(2, "User 2");
        hashMap.put(3, "User 3");
    }

    private void getPostApi() {
        App.api.getPost(FormFragment.GROUP_ID).enqueue(new Callback<List<PostModel>>() {
            @Override
            public void onResponse(Call<List<PostModel>> call, Response<List<PostModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setPostModelList(response.body());
                    adapter.setHashMap(hashMap);
                }
            }

            @Override
            public void onFailure(Call<List<PostModel>> call, Throwable t) {
            }
        });
    }

    @Override
    public void onClick(PostModel postModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("key", postModel);
        Navigation.findNavController(requireView()).navigate(R.id.action_postsFragment_to_formFragment, bundle);
    }

    @Override
    public void onLongClick(PostModel postModel) {
        alertDialog(postModel);
    }

    private void alertDialog(PostModel postModel) {
        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(requireContext());
        materialAlertDialogBuilder.setTitle("Подтвердите выбор");
        materialAlertDialogBuilder.setMessage("Вы точно хотите удалить лист?");
        materialAlertDialogBuilder.setNegativeButton("Отменить", null);
        materialAlertDialogBuilder.setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                delete(postModel);
            }
        }).show();
    }

    private void delete(PostModel postModel) {
        App.api.deletePost(postModel.getId()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                getPostApi();
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
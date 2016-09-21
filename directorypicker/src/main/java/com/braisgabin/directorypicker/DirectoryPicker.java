package com.braisgabin.directorypicker;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileFilter;

public class DirectoryPicker extends DialogFragment implements AdapterView.OnItemClickListener {
  private static final String ARG_ROOT = "ARG_ROOT";
  private static final String ARG_RELATIVE_PATH = "ARG_RELATIVE_PATH";

  private final FileFilter ff = new FileFilter() {
    @Override
    public boolean accept(File pathname) {
      return pathname.isDirectory();
    }
  };

  private Toolbar toolbar;
  private ListView listView;

  private File root;
  private File actualPath;

  private OnDirectoryClickedListener listener;

  public static DirectoryPicker newInstance(File root, String relativePath) {
    return newInstance(root.getAbsolutePath(), relativePath);
  }

  public static DirectoryPicker newInstance(String root, String relativePath) {
    final Bundle args = new Bundle();
    args.putString(ARG_ROOT, root);
    args.putString(ARG_RELATIVE_PATH, relativePath);

    final DirectoryPicker fragment = new DirectoryPicker();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    final Fragment parentFragment = getParentFragment();
    if (parentFragment != null) {
      try {
        listener = (OnDirectoryClickedListener) parentFragment;
      } catch (ClassCastException e) {
        throw new ClassCastException(parentFragment.toString() + " must implement OnDirectoryClickedListener");
      }
    } else {
      try {
        listener = (OnDirectoryClickedListener) getActivity();
      } catch (ClassCastException e) {
        throw new ClassCastException(getActivity().toString() + " must implement OnDirectoryClickedListener");
      }
    }
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    final Bundle arguments = getArguments();
    this.root = new File(arguments.getString(ARG_ROOT));
    this.actualPath = new File(root, arguments.getString(ARG_RELATIVE_PATH));
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    final Dialog dialog = super.onCreateDialog(savedInstanceState);

    setStyle(DialogFragment.STYLE_NO_TITLE, 0);

    return dialog;
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.fragment_directory_picker, container, false);

    this.toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (!root.equals(actualPath)) {
          refreshActualPath(actualPath.getParentFile());
        }
      }
    });
    this.listView = (ListView) view.findViewById(android.R.id.list);
    listView.setOnItemClickListener(this);
    refreshActualPath(actualPath);
    view.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        listener.onDirectoryClicked(DirectoryPicker.this, actualPath);
        dismiss();
      }
    });
    view.findViewById(R.id.canel).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dismiss();
      }
    });

    return view;
  }

  private void refreshActualPath(File file) {
    actualPath = file;
    if (root.equals(file)) {
      toolbar.setNavigationIcon(null);
    } else {
      toolbar.setNavigationIcon(R.drawable.ic_arrow_upward_black_24dp);
    }
    String title = file.getAbsolutePath().substring(root.getAbsolutePath().length());
    title = title.isEmpty() ? "/" : title;
    toolbar.setTitle(title);
    File[] files = file.listFiles(ff);
    files = files == null ? new File[0] : files;
    listView.setAdapter(new DirectoryAdapter(getActivity().getLayoutInflater(), files));
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    refreshActualPath((File) parent.getItemAtPosition(position));
  }

  public interface OnDirectoryClickedListener {
    void onDirectoryClicked(DirectoryPicker picker, File file);
  }

  static class DirectoryAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private final File[] direcories;

    DirectoryAdapter(LayoutInflater inflater, File[] direcories) {
      this.inflater = inflater;
      this.direcories = direcories;
    }


    @Override
    public int getCount() {
      return direcories.length;
    }

    @Override
    public File getItem(int position) {
      return direcories[position];
    }

    @Override
    public long getItemId(int position) {
      return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      if (convertView == null) {
        convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
      }
      ((TextView) convertView).setText(direcories[position].getName());
      return convertView;
    }
  }
}

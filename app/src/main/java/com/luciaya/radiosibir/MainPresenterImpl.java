package com.luciaya.radiosibir;

public class MainPresenterImpl implements MainContract.Presenter {
    private MainContract.View mView;

    public MainPresenterImpl(MainContract.View view) {
        mView = view;
    }


    @Override
    public void onDestroy() {
        mView = null;
    }

    @Override
    public void onPauseBtnClicked() {
        mView.pause();
    }

    @Override
    public void onPlayBtnClicked() {
        mView.play();
    }
}

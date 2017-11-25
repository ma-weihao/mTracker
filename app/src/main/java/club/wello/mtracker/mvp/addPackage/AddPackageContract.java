package club.wello.mtracker.mvp.addPackage;

import club.wello.mtracker.mvp.BasePresenter;
import club.wello.mtracker.mvp.BaseView;

/**
 * mvp contract for AddActivity
 * Created by maweihao on 2017/10/23.
 */

public interface AddPackageContract {

    interface View extends BaseView<Presenter>{
        void showNumberExistError();

        void showNumberError();

        void setProgressIndicator(boolean loading);

        void showPackagesList();

        void showNetworkError();

    }

    interface Presenter extends BasePresenter{
        void savePackage(String number, String name);
    }
}

package com.qzct.immediatechoice.util;

import com.qzct.immediatechoice.domain.Question;
import com.qzct.immediatechoice.domain.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tsh2 on 2017/4/16.
 */

public interface MyCallback {

    interface FriendInfoCallBack {
        void onSuccess(List<User> userList);

        void onError(Throwable ex);

    }

    interface LoadQuestionCallBack {
        int getPage(boolean isRefresh);

        int getUserId();

        void onSuccess(boolean isRefresh, ArrayList<Question> list);

        void onDataIsNull();

        void onError(Throwable ex);

        void onFinished();

    }

    interface InitRefreshAndLoadCallBack {

        /**
         * 下拉刷新
         */
        void refresh();

        /**
         * 上拉加载
         */
        void loadMore();
    }
}

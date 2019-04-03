package com.qzct.immediatechoice.util;

import com.qzct.immediatechoice.domain.Question;
import com.qzct.immediatechoice.domain.Questionnaire;
import com.qzct.immediatechoice.domain.User;

import org.json.JSONArray;

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

    interface UpdateFriendCallback {
        String getUpdateType();

        void onSuccess(String msg);

        void onError(Throwable ex);

    }

    interface GetQuestionnaireCallback {
        int getQuestionnaireId();

        int getUserId();

        void onSuccess(Questionnaire questionnaire);

        void onSuccess(List<Questionnaire> list);

        void onError(Throwable ex);

    }

    interface LoginCallback {

        void onError(Throwable ex);

        void onSuccess(String result);
    }
    interface PushQuestionnaireCallback {
        void onSuccess();

        void onFailure();

        void onError(Throwable ex);
    }

    interface GetQuestionnaireIdsCallback {
        void onSuccess(List<Integer> idList);

        void onError(Throwable ex);
    }

    interface ChooseQuestionOptionCallback {
        void onSuccess();

        void onError(Throwable ex);
    }

    public interface OtherInfoCallBack {
        String getUserId();

        void onSuccess(User other);

        void onError(Throwable ex);
    }

    public interface TopicsCallBack {
        void onSuccess(JSONArray jsonArray);

        void onFail();

        void onIsNull();

        void onError(Throwable ex);
    }
}

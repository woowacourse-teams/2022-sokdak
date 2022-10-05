import { useContext, lazy, Suspense } from 'react';
import { useQueryClient } from 'react-query';
import { Route, Routes } from 'react-router-dom';

import Prompt from './components/Prompt';
import Header from '@/components/Header';
import Snackbar from '@/components/Snackbar';
import Spinner from '@/components/Spinner';

import AuthContext from './context/Auth';

import useInstallApp from './hooks/useInstallApp';
import useSnackbar from './hooks/useSnackbar';

import PATH from './constants/path';
import { MUTATION_KEY } from './constants/queries';
import SNACKBAR_MESSAGE from './constants/snackbar';

import PrivateRoute from './routes/PrivateRoute';
import PublicRoute from './routes/PublicRoute';
import styled from '@emotion/styled';

const BoardPage = lazy(() => import(/* webpackChunkName: "BoardPage" */ '@/pages/BoardPage'));
const CreatePostPage = lazy(() => import(/* webpackChunkName: "CreatePostPage" */ '@/pages/CreatePostPage'));
const HashTagPage = lazy(() => import(/* webpackChunkName: "HashTagPage" */ '@/pages/HashTagPage'));
const LoginPage = lazy(() => import(/* webpackChunkName: "LoginPage" */ '@/pages/LoginPage'));
const MainPage = lazy(() => import(/* webpackChunkName: "MainPage" */ '@/pages/MainPage'));
const NotFoundPage = lazy(() => import(/* webpackChunkName: "NotFoundPage" */ '@/pages/NotFoundPage'));
const NotificationPage = lazy(() => import(/* webpackChunkName: "NotificationPage" */ '@/pages/NotificationPage'));
const PostPage = lazy(() => import(/* webpackChunkName: "PostPage" */ '@/pages/PostPage'));
const ProfilePage = lazy(() => import(/* webpackChunkName: "ProfilePage" */ '@/pages/ProfilePage'));
const SignUpPage = lazy(() => import(/* webpackChunkName: "SignUpPage" */ '@/pages/SignUpPage'));
const UpdatePostPage = lazy(() => import(/* webpackChunkName: "UpdatePostPage" */ '@/pages/UpdatePostPage'));

const App = () => {
  const { isVisible, message, showSnackbar } = useSnackbar();
  const { installable, setInstallable, installApp } = useInstallApp();
  const { setIsLogin, setUsername } = useContext(AuthContext);
  const queryClient = useQueryClient();

  Object.values(MUTATION_KEY).forEach(KEY => {
    queryClient.setMutationDefaults(KEY, {
      onError: err => {
        if (err.response?.status === 401) {
          showSnackbar(SNACKBAR_MESSAGE.NOT_LOGIN);
          setIsLogin(false);
          setUsername('');
          return;
        }
        showSnackbar(err.response?.data.message!);
      },
    });
  });

  return (
    <>
      <Header />
      <Suspense fallback={<FallbackSpinner />}>
        <Routes>
          <Route path={PATH.HOME} element={<MainPage />} />
          <Route path={`${PATH.POST}/:id`} element={<PostPage />} />
          <Route path={PATH.UPDATE_POST} element={<UpdatePostPage />} />
          <Route path={`${PATH.BOARD}/:id`} element={<BoardPage />} />
          <Route path={`${PATH.HASHTAG}/:name`} element={<HashTagPage />} />
          <Route path="*" element={<NotFoundPage />} />
          <Route element={<PrivateRoute />}>
            <Route path={PATH.CREATE_POST} element={<CreatePostPage />} />
            <Route path={PATH.PROFILE} element={<ProfilePage />} />
            <Route path={PATH.NOTIFICATION} element={<NotificationPage />} />
          </Route>
          <Route element={<PublicRoute />}>
            <Route path={PATH.LOGIN} element={<LoginPage />} />
            <Route path={PATH.SIGN_UP} element={<SignUpPage />} />
          </Route>
        </Routes>
      </Suspense>
      {isVisible && <Snackbar message={message} />}
      {installable && (
        <Prompt
          name="install"
          message="속닥속닥 앱을 설치하시겠습니까?"
          confirmText="설치"
          cancelText="취소"
          confirm={installApp}
          cancel={() => setInstallable(false)}
          visible={installable}
          setVisible={setInstallable}
        />
      )}
    </>
  );
};

const FallbackSpinner = styled(Spinner)`
  width: 100%;
  height: 500px;
  justify-content: center;
  align-items: center;
`;

export default App;

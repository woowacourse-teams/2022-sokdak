import { useContext, lazy, Suspense } from 'react';
import { useQueryClient } from 'react-query';
import { Route, Routes } from 'react-router-dom';

import Header from '@/components/Header';
import Snackbar from '@/components/Snackbar';
import Spinner from '@/components/Spinner';

import AuthContext from './context/Auth';

import useSnackbar from './hooks/useSnackbar';

import PATH from './constants/path';
import { MUTATION_KEY } from './constants/queries';
import SNACKBAR_MESSAGE from './constants/snackbar';

import PrivateRoute from './routes/PrivateRoute';
import PublicRoute from './routes/PublicRoute';
import styled from '@emotion/styled';

const BoardPage = lazy(() => import('@/pages/BoardPage'));
const CreatePostPage = lazy(() => import('@/pages/CreatePostPage'));
const HashTagPage = lazy(() => import('@/pages/HashTagPage'));
const LoginPage = lazy(() => import('@/pages/LoginPage'));
const MainPage = lazy(() => import('@/pages/MainPage'));
const NotFoundPage = lazy(() => import('@/pages/NotFoundPage'));
const NotificationPage = lazy(() => import('@/pages/NotificationPage'));
const PostPage = lazy(() => import('@/pages/PostPage'));
const ProfilePage = lazy(() => import('@/pages/ProfilePage'));
const SignUpPage = lazy(() => import('@/pages/SignUpPage'));
const UpdatePostPage = lazy(() => import('@/pages/UpdatePostPage'));

const App = () => {
  const { isVisible, message, showSnackbar } = useSnackbar();
  const { setIsLogin, setUserName } = useContext(AuthContext);
  const queryClient = useQueryClient();

  Object.values(MUTATION_KEY).forEach(KEY => {
    queryClient.setMutationDefaults(KEY, {
      onError: err => {
        if (err.response?.status === 401) {
          showSnackbar(SNACKBAR_MESSAGE.NOT_LOGIN);
          setIsLogin(false);
          setUserName('');
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

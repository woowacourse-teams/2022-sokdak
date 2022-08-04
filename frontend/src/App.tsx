import { useContext } from 'react';
import { useQueryClient } from 'react-query';
import { Route, Routes } from 'react-router-dom';

import BoardPage from './pages/BoardPage';
import CreatePostPage from './pages/CreatePostPage';
import HashTagPage from './pages/HashTagPage';
import LoginPage from './pages/LoginPage';
import MainPage from './pages/MainPage';
import NotFoundPage from './pages/NotFoundPage';
import PostPage from './pages/PostPage';
import SignUpPage from './pages/SignUpPage';
import UpdatePostPage from './pages/UpdatePostPage';

import Snackbar from './components/Snackbar';
import Header from '@/components/Header';

import AuthContext from './context/Auth';

import useSnackbar from './hooks/useSnackbar';

import PATH from './constants/path';
import { MUTATION_KEY } from './constants/queries';

const App = () => {
  const { isVisible, message, showSnackbar } = useSnackbar();
  const { setIsLogin, setUserName } = useContext(AuthContext);
  const queryClient = useQueryClient();

  Object.values(MUTATION_KEY).forEach(KEY => {
    queryClient.setMutationDefaults(KEY, {
      onError: err => {
        if (err.response?.status === 401) {
          showSnackbar('로그인을 해주세요');
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
      <Routes>
        <Route path={PATH.HOME} element={<MainPage />} />
        <Route path={PATH.CREATE_POST} element={<CreatePostPage />} />
        <Route path={`${PATH.POST}/:id`} element={<PostPage />} />
        <Route path={PATH.LOGIN} element={<LoginPage />} />
        <Route path={PATH.SIGN_UP} element={<SignUpPage />} />
        <Route path={PATH.UPDATE_POST} element={<UpdatePostPage />} />
        <Route path={`${PATH.BOARD}/:id`} element={<BoardPage />} />
        <Route path={`${PATH.HASHTAG}/:name`} element={<HashTagPage />} />
        <Route path="*" element={<NotFoundPage />} />
      </Routes>
      {isVisible && <Snackbar message={message} />}
    </>
  );
};

export default App;

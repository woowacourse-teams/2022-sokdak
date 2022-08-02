import { Route, Routes } from 'react-router-dom';

import CreatePostPage from './pages/CreatePostPage';
import LoginPage from './pages/LoginPage';
import MainPage from './pages/MainPage';
import NotFoundPage from './pages/NotFoundPage';
import PostPage from './pages/PostPage';
import SignUpPage from './pages/SignUpPage';
import UpdatePostPage from './pages/UpdatePostPage';

import Snackbar from './components/Snackbar';
import Header from '@/components/Header';

import useSnackbar from './hooks/useSnackbar';

import PATH from './constants/path';

const App = () => {
  const { isVisible, message } = useSnackbar();

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
        <Route path="*" element={<NotFoundPage />} />
      </Routes>
      {isVisible && <Snackbar message={message} />}
    </>
  );
};

export default App;

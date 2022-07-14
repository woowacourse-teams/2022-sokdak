import { useContext } from 'react';
import { Route, Routes } from 'react-router-dom';

import CreatePostPage from './pages/CreatePostPage';
import LoginPage from './pages/LoginPage';
import MainPage from './pages/MainPage';
import NotFoundPage from './pages/NotFoundPage';
import PostPage from './pages/PostPage';

import Snackbar from './components/Snackbar';
import Header from '@/components/Header';

import SnackbarContext from './context/Snackbar';

import PATH from './constants/path';

const App = () => {
  const { isVisible, message } = useContext(SnackbarContext);

  return (
    <>
      <Header />
      <Routes>
        <Route path={PATH.HOME} element={<MainPage />} />
        <Route path={PATH.CREATE_POST} element={<CreatePostPage />} />
        <Route path={`${PATH.POST}/:id`} element={<PostPage />} />
        <Route path={PATH.LOGIN} element={<LoginPage />} />
        <Route path="*" element={<NotFoundPage />} />
      </Routes>
      {isVisible && <Snackbar message={message} />}
    </>
  );
};

export default App;

import { useContext } from 'react';
import { Route, Routes } from 'react-router-dom';

import CreatePostPage from './pages/CreatePostPage';
import MainPage from './pages/MainPage';
import NotFoundPage from './pages/NotFoundPage';
import PostPage from './pages/PostPage';

import Snackbar from './components/Snackbar';
import Header from '@/components/Header';

import SnackbarContext from './context/Snackbar';

const App = () => {
  const { isVisible, message } = useContext(SnackbarContext);

  return (
    <>
      <Header />
      <Routes>
        <Route path="/" element={<MainPage />} />
        <Route path="/post/write" element={<CreatePostPage />} />
        <Route path="/post/:id" element={<PostPage />} />
        <Route path="*" element={<NotFoundPage />} />
      </Routes>
      {isVisible && <Snackbar message={message} />}
    </>
  );
};

export default App;

import Header from '@/components/Header';
import { useContext } from 'react';
import Snackbar from './components/Snackbar';
import SnackbarContext from './context/Snackbar';
import { Route, Routes } from 'react-router-dom';
import MainPage from './pages/MainPage';
import PostPage from './pages/PostPage';
import CreatePostPage from './pages/CreatePostPage';
import NotFoundPage from './pages/NotFoundPage';

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
import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { Layout, Text, Flex } from '../components';
import authService from '../services/authService';

const VerifyPage = ({ token }) => {
  const [isLoading, setLoading] = useState(true);
  const [verificationError, setError] = useState('');

  useEffect(() => {
    let mounted = true;
    const verifyUser = async () => {
      const response = await authService.verify({ token });
      if (mounted) {
        setLoading(false);
        if (response.status !== 200) {
          setError(response.error);
        }
      }
    };

    verifyUser();
    return () => (mounted = false);
  }, []);
  return (
    <Layout isLoading={isLoading}>
      <Flex justify="justify-center" align="items-center" direction="flex-col">
        {verificationError ? (
          <>
            <Text size="text-5xl" type="error" className="pb-6 text-center">
              {verificationError}
            </Text>
            <Link to="/">
              <Text type="link" className="inline-block w-full text-center">
                Go to Homepage
              </Text>
            </Link>
          </>
        ) : (
          <>
            <Text size="text-5xl" type="success" className="pb-6 text-center">
              Verification Success!
            </Text>
            <Link to="/login">
              <Text type="link" className="inline-block w-full text-center">
                Go to login
              </Text>
            </Link>
          </>
        )}
      </Flex>
    </Layout>
  );
};

export default VerifyPage;

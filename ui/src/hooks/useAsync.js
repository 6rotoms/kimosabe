import { useState, useEffect } from 'react';
/**
 * hook for setting up an async call that can be called using a trigger function
 * @param  asyncFn is the async call that is made when trigger is called
 * @param  runOnMount is the flag that determines whether or not async call should be made on first hook call/mount
 */
export default function useAsync(asyncFn, { runOnMount = true } = {}) {
  const [loading, setLoading] = useState(false);
  const [response, setResponse] = useState(null);
  const [error, setError] = useState(null);
  const [trigger, setTrigger] = useState(runOnMount);

  useEffect(() => {
    let mounted = true;
    const fetchData = async () => {
      setLoading(true);
      const res = await asyncFn();
      if (!mounted) return;
      setTrigger(!trigger);
      if (res.status !== 200) {
        setError(res.error);
      } else if (res.status === 200) {
        setResponse(res);
        setError(null);
      }
      setLoading(false);
    };
    if (trigger) fetchData();
    return () => (mounted = false);
  }, [trigger]);

  function triggerCall() {
    setTrigger(!trigger);
  }

  return { loading, triggerCall, response, error };
}

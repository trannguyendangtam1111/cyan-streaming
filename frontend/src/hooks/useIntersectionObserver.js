import { useEffect, useRef } from "react";

function useIntersectionObserver(onIntersect, options = {}) {
  const elementRef = useRef(null);

  useEffect(() => {
    const element = elementRef.current;
    if (!element) {
      return undefined;
    }

    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            onIntersect();
          }
        });
      },
      {
        root: options.root ?? null,
        rootMargin: options.rootMargin ?? "200px",
        threshold: options.threshold ?? 0.1,
      },
    );

    observer.observe(element);

    return () => observer.disconnect();
  }, [onIntersect, options.root, options.rootMargin, options.threshold]);

  return elementRef;
}

export default useIntersectionObserver;

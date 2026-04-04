import auth from '@react-native-firebase/auth';

// let cachedToken: string | null = null;

export const loginUser = async (email: string, password: string) => {
  try {
    const userCredential = await auth().signInWithEmailAndPassword(email, password);

    const token = await userCredential.user.getIdToken(true);

    // cachedToken = token;

    console.log("🔥 Token:", token);
    console.log("👤 UID:", userCredential.user.uid);

    return {
      success: true,
      token,
      user: userCredential.user,
    };

  } catch (error: any) {
    console.log("❌ Login failed:", error.code);

    return { success: false, message: error.message };
  }
};

// 🔥 Used everywhere (VERY IMPORTANT)
export const getAuthToken = async (): Promise<string | null> => {
  try {
    const currentUser = auth().currentUser;

    if (!currentUser) return null;

    const token = await currentUser.getIdToken(true);
    // cachedToken = token;

    return token;

  } catch (error) {
    console.log("❌ Token error:", error);
    return null;
  }
};
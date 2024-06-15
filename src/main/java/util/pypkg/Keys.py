def get(key):
    """
    Retrieves a secret value based on the provided key.

    Args:
        key: The key to identify the desired secret.

    Returns:
        The secret value associated with the key, or None if not found.
    """
    if key == "gemini":
        return "AIzaSyAylywSkfIpglOjxkRKsVd_VZS_h5J-rao"
    else:
        return None

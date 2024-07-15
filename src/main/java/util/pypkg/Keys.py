import configparser
import logging
import sqlite3
from typing import Optional

# Configure logging
logging.basicConfig(level=logging.ERROR)
logger = logging.getLogger(__name__)


class Keys:
    _instance = None
    _props = None

    def __new__(cls):
        if cls._instance is None:
            cls._instance = super().__new__(cls)
            cls._instance.load_properties()
        return cls._instance

    @classmethod
    def get(cls, apikey: str) -> Optional[str]:
        conn = None
        cursor = None
        result = None

        try:
            # Constructing the full database URL
            conn_str = f"{cls._props['DB_URL1']}{cls._props['PASSWORD']}{cls._props['DB_URL2']}"
            conn = sqlite3.connect(conn_str)
            cursor = conn.cursor()
            cursor.execute("SELECT apikey FROM apikeys WHERE service = ?", (apikey,))
            result = cursor.fetchone()

            if result is not None:
                return result[0]
            else:
                logger.info(f"No API key found for service: {apikey}")
        except Exception as e:
            logger.error(f"Error retrieving API key: {str(e)}")

        return None

    def load_properties(self):
        config = configparser.ConfigParser()
        try:
            with open('db.properties', 'r') as stream:
                config.read_file(stream)
            self._props = dict(config['DEFAULT'])
        except FileNotFoundError:
            logger.error("db.properties file not found")
        except Exception as e:
            logger.error(f"Error loading db.properties: {str(e)}")


if __name__ == "__main__":
    # Example usage
    key = Keys.get("gemini")
    print(key)
